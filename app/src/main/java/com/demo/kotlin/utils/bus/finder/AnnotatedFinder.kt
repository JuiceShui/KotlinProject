package com.demo.kotlin.utils.bus.finder

import com.demo.kotlin.utils.bus.annotation.Produce
import com.demo.kotlin.utils.bus.annotation.Subscribe
import com.demo.kotlin.utils.bus.annotation.Tag
import com.demo.kotlin.utils.bus.entity.EventType
import com.demo.kotlin.utils.bus.entity.ProducerEvent
import com.demo.kotlin.utils.bus.entity.SubscriberEvent
import com.demo.kotlin.utils.bus.thread.EventThread
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object AnnotatedFinder {
    /**
     * Cache event bus producer methods for each class.
     */
    private val PRODUCERS_CACHE: ConcurrentMap<Class<*>, MutableMap<EventType, SourceMethod>> =
        ConcurrentHashMap()

    /**
     * Cache event bus subscriber methods for each class.
     */
    private val SUBSCRIBERS_CACHE: ConcurrentMap<Class<*>, MutableMap<EventType, MutableSet<SourceMethod>>> =
        ConcurrentHashMap()

    private fun loadAnnotatedProducerMethods(
        listenerClass: Class<*>,
        producerMethods: MutableMap<EventType, SourceMethod>
    ) {
        val subscriberMethods: MutableMap<EventType, MutableSet<SourceMethod>> = HashMap()
        loadAnnotatedMethods(listenerClass, producerMethods, subscriberMethods)
    }

    private fun loadAnnotatedSubscriberMethods(
        listenerClass: Class<*>,
        subscriberMethods: MutableMap<EventType, MutableSet<SourceMethod>>
    ) {
        val producerMethods: MutableMap<EventType, SourceMethod> = HashMap()
        loadAnnotatedMethods(listenerClass, producerMethods, subscriberMethods)
    }

    /**
     * Load all methods annotated with [Produce] or [Subscribe] into their respective caches for the
     * specified class.
     */
    private fun loadAnnotatedMethods(
        listenerClass: Class<*>,
        producerMethods: MutableMap<EventType, SourceMethod>,
        subscriberMethods: MutableMap<EventType, MutableSet<SourceMethod>>
    ) {
        for (method: Method in listenerClass.declaredMethods) {
            // The compiler sometimes creates synthetic bridge methods as part of the
            // type erasure process. As of JDK8 these methods now include the same
            // annotations as the original declarations. They should be ignored for
            // subscribe/produce.
            if (method.isBridge) {
                continue
            }
            if (method.isAnnotationPresent(Subscribe::class.java)) {
                val parameterTypes = method.parameterTypes
                if (parameterTypes.size != 1) {
                    throw IllegalArgumentException(
                        "Method " + method + " has @Subscribe annotation but requires "
                                + parameterTypes.size + " arguments.  Methods must require a single argument."
                    )
                }
                val parameterClazz = parameterTypes[0]
                if (parameterClazz.isInterface) {
                    throw IllegalArgumentException(
                        ("Method " + method + " has @Subscribe annotation on " + parameterClazz
                                + " which is an interface.  Subscription must be on a concrete class type.")
                    )
                }
                if ((method.modifiers and Modifier.PUBLIC) == 0) {
                    throw IllegalArgumentException(
                        ("Method " + method + " has @Subscribe annotation on " + parameterClazz
                                + " but is not 'public'.")
                    )
                }
                val annotation = method.getAnnotation(Subscribe::class.java)
                val thread: EventThread = annotation.thread
                val tags: Array<Tag> = annotation.tags
                var tagLength = tags.size
                do {
                    var tag: String? = Tag.DEFAULT
                    if (tagLength > 0) {
                        tag = tags[tagLength - 1].value
                    }
                    val type = EventType(tag, parameterClazz)
                    var methods = subscriberMethods[type]
                    if (methods == null) {
                        methods = HashSet()
                        subscriberMethods[type] = methods
                    }
                    methods.add(SourceMethod(thread, method))
                    tagLength--
                } while (tagLength > 0)
            } else if (method.isAnnotationPresent(Produce::class.java)) {
                val parameterTypes = method.parameterTypes
                if (parameterTypes.size != 0) {
                    throw IllegalArgumentException(
                        ("Method " + method + "has @Produce annotation but requires "
                                + parameterTypes.size + " arguments.  Methods must require zero arguments.")
                    )
                }
                if (method.returnType == Void::class.java) {
                    throw IllegalArgumentException(
                        ("Method " + method
                                + " has a return type of void.  Must declare a non-void type.")
                    )
                }
                val parameterClazz = method.returnType
                if (parameterClazz.isInterface) {
                    throw IllegalArgumentException(
                        ("Method " + method + " has @Produce annotation on " + parameterClazz
                                + " which is an interface.  Producers must return a concrete class type.")
                    )
                }
                if ((parameterClazz == Void.TYPE)) {
                    throw IllegalArgumentException("Method $method has @Produce annotation but has no return type.")
                }
                if ((method.modifiers and Modifier.PUBLIC) == 0) {
                    throw IllegalArgumentException(
                        ("Method " + method + " has @Produce annotation on " + parameterClazz
                                + " but is not 'public'.")
                    )
                }
                val annotation = method.getAnnotation(Produce::class.java)
                val thread: EventThread = annotation.thread
                val tags: Array<Tag> = annotation.tags
                var tagLength = (tags?.size ?: 0)
                do {
                    var tag: String? = Tag.DEFAULT
                    if (tagLength > 0) {
                        tag = tags[tagLength - 1].value
                    }
                    val type = EventType(tag, parameterClazz)
                    if (producerMethods.containsKey(type)) {
                        throw IllegalArgumentException("Producer for type $type has already been registered.")
                    }
                    producerMethods[type] = SourceMethod(thread, method)
                    tagLength--
                } while (tagLength > 0)
            }
        }
        PRODUCERS_CACHE[listenerClass] = producerMethods
        SUBSCRIBERS_CACHE[listenerClass] = subscriberMethods
    }

    /**
     * This implementation finds all methods marked with a [Produce] annotation.
     */
    fun findAllProducers(listener: Any): Map<EventType, ProducerEvent> {
        val listenerClass: Class<*> = listener.javaClass
        val producersInMethod: MutableMap<EventType, ProducerEvent> = HashMap()
        var methods = PRODUCERS_CACHE[listenerClass]
        if (null == methods) {
            methods = HashMap()
            loadAnnotatedProducerMethods(listenerClass, methods)
        }
        if (!methods.isEmpty()) {
            for (e: Map.Entry<EventType, SourceMethod> in methods.entries) {
                val producer = ProducerEvent(listener, e.value.method, e.value.thread)
                producersInMethod[e.key] = producer
            }
        }
        return producersInMethod
    }

    /**
     * This implementation finds all methods marked with a [Subscribe] annotation.
     */
    fun findAllSubscribers(listener: Any): Map<EventType, Set<SubscriberEvent>> {
        val listenerClass: Class<*> = listener.javaClass
        val subscribersInMethod: MutableMap<EventType, Set<SubscriberEvent>> = HashMap()
        var methods = SUBSCRIBERS_CACHE[listenerClass]
        if (null == methods) {
            methods = HashMap()
            loadAnnotatedSubscriberMethods(listenerClass, methods)
        }
        if (!methods.isEmpty()) {
            for (e: Map.Entry<EventType, Set<SourceMethod>> in methods.entries) {
                val subscribers: MutableSet<SubscriberEvent> = HashSet()
                for (m: SourceMethod in e.value) {
                    subscribers.add(SubscriberEvent(listener, m.method, m.thread))
                }
                subscribersInMethod[e.key] = subscribers
            }
        }
        return subscribersInMethod
    }

    private class SourceMethod(
        val thread: EventThread,
        val method: Method
    )
}
