package com.demo.kotlin.utils.bus

import com.demo.kotlin.utils.bus.annotation.Tag
import com.demo.kotlin.utils.bus.entity.DeadEvent
import com.demo.kotlin.utils.bus.entity.EventType
import com.demo.kotlin.utils.bus.entity.ProducerEvent
import com.demo.kotlin.utils.bus.entity.SubscriberEvent
import com.demo.kotlin.utils.bus.finder.Finder
import com.demo.kotlin.utils.bus.thread.ThreadEnforcer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArraySet

class Bus internal constructor(
    /**
     * Thread enforcer for register, unregister, and posting events.
     */
    private val enforcer: ThreadEnforcer,
    /**
     * Identifier used to differentiate the event bus instance.
     */
    private var identifier: String, finder: Finder
) {
    /**
     * All registered event subscribers, indexed by event type.
     */
    private val subscribersByType: ConcurrentMap<EventType, MutableSet<SubscriberEvent?>> =
        ConcurrentHashMap()

    /**
     * All registered event producers, index by event type.
     */
    private val producersByType: ConcurrentMap<EventType, ProducerEvent?> = ConcurrentHashMap()

    /**
     * Used to find subscriber methods in register and unregister.
     */
    private val finder: Finder
    private val flattenHierarchyCache: ConcurrentMap<Class<*>, Set<Class<*>>> = ConcurrentHashMap()
    /**
     * Creates a new Bus with the given `identifier` that enforces actions on the main thread.
     *
     * @param identifier a brief name for this bus, for debugging purposes.  Should be a valid Java identifier.
     */
    /**
     * Creates a new Bus named "default" that enforces actions on the main thread.
     */
    @JvmOverloads
    constructor(identifier: String = DEFAULT_IDENTIFIER) : this(ThreadEnforcer.MAIN, identifier) {
    }
    /**
     * Creates a new Bus with the given `enforcer` for actions and the given `identifier`.
     *
     * @param enforcer   Thread enforcer for register, unregister, and post actions.
     * @param identifier A brief name for this bus, for debugging purposes.  Should be a valid Java identifier.
     */
    /**
     * Creates a new Bus named "default" with the given `enforcer` for actions.
     *
     * @param enforcer Thread enforcer for register, unregister, and post actions.
     */
    @JvmOverloads
    constructor(enforcer: ThreadEnforcer, identifier: String = DEFAULT_IDENTIFIER) : this(
        enforcer,
        identifier,
        Finder.ANNOTATED
    ) {
    }

    override fun toString(): String {
        return "[Bus \"$identifier\"]"
    }

    /**
     * Registers all subscriber methods on `object` to receive events and producer methods to provide events.
     *
     *
     * If any subscribers are registering for types which already have a producer they will be called immediately
     * with the result of calling that producer.
     *
     *
     * If any producers are registering for types which already have subscribers, each subscriber will be called with
     * the value from the result of calling the producer.
     *
     * @param object object whose subscriber methods should be registered.
     * @throws NullPointerException if the object is null.
     */
    fun register(`object`: Any?) {
        if (`object` == null) {
            throw NullPointerException("Object to register must not be null.")
        }
        enforcer.enforce(this)
        val foundProducers = finder.findAllProducers(`object`)
        for (type: EventType? in foundProducers!!.keys) {
            val producer = foundProducers?.get(type)
            val previousProducer = producersByType.putIfAbsent(type, producer)
            //checking if the previous producer existed
            if (previousProducer != null) {
                throw IllegalArgumentException(
                    "Producer method for type " + type
                            + " found on type " + producer!!.target.javaClass
                            + ", but already registered by type " + previousProducer.target.javaClass + "."
                )
            }
            val subscribers: Set<SubscriberEvent?>? = subscribersByType[type]
            if (subscribers != null && !subscribers.isEmpty()) {
                for (subscriber: SubscriberEvent? in subscribers) {
                    dispatchProducerResult(subscriber, producer)
                }
            }
        }
        val foundSubscribersMap = finder.findAllSubscribers(`object`)
        for (type: EventType? in foundSubscribersMap!!.keys) {
            var subscribers = subscribersByType[type]
            if (subscribers == null) {
                //concurrent put if absent
                val SubscribersCreation: MutableSet<SubscriberEvent?> = CopyOnWriteArraySet()
                subscribers = subscribersByType.putIfAbsent(type, SubscribersCreation)
                if (subscribers == null) {
                    subscribers = SubscribersCreation
                }
            }
            val foundSubscribers = (foundSubscribersMap[type])!!
            if (!subscribers.addAll(foundSubscribers)) {
                throw IllegalArgumentException("Object already registered.")
            }
        }
        for (entry: Map.Entry<EventType?, Set<SubscriberEvent?>?> in foundSubscribersMap.entries) {
            val type = entry.key
            val producer = producersByType[type]
            if (producer != null && producer.isValid) {
                val subscriberEvents = entry.value
                for (subscriberEvent: SubscriberEvent? in subscriberEvents!!) {
                    if (!producer.isValid) {
                        break
                    }
                    if (subscriberEvent!!.isValid) {
                        dispatchProducerResult(subscriberEvent, producer)
                    }
                }
            }
        }
    }

    private fun dispatchProducerResult(
        subscriberEvent: SubscriberEvent?,
        producer: ProducerEvent?
    ) {
        producer!!.produce().subscribe { event -> event?.let { dispatch(it, subscriberEvent) } }
    }

    /**
     * Whether all the subscriber methods on `object` to receive events and producer methods to provide events has registered.
     *
     *
     * If any subscribers and producers has registered, it will return true, alse false.
     *
     * @param object object whose subscriber methods should be registered.
     * @throws NullPointerException if the object is null.
     */
    @Deprecated("")
    fun hasRegistered(`object`: Any?): Boolean {
        if (`object` == null) {
            throw NullPointerException("Object to register must not be null.")
        }
        var hasProducerRegistered = false
        var hasSubscriberRegistered = false
        val foundProducers = finder.findAllProducers(`object`)
        for (type: EventType? in foundProducers!!.keys) {
            val producer = foundProducers[type]
            hasProducerRegistered = producersByType.containsValue(producer)
            if (hasProducerRegistered) {
                break
            }
        }
        if (!hasProducerRegistered) {
            val foundSubscribersMap = finder.findAllSubscribers(`object`)
            for (type: EventType? in foundSubscribersMap!!.keys) {
                val subscribers: Set<SubscriberEvent?>? = subscribersByType[type]
                if (subscribers != null && subscribers.size > 0) {
                    val foundSubscribers = (foundSubscribersMap[type])!!
                    // check the first subscriber, Zzzzz...
                    val foundSubscriber =
                        if (!foundSubscribers.isEmpty()) foundSubscribers.iterator()
                            .next() else null
                    hasSubscriberRegistered = subscribers.contains(foundSubscriber)
                    if (hasSubscriberRegistered) {
                        break
                    }
                }
            }
        }
        return hasProducerRegistered || hasSubscriberRegistered
    }

    /**
     * Unregisters all producer and subscriber methods on a registered `object`.
     *
     * @param object object whose producer and subscriber methods should be unregistered.
     * @throws IllegalArgumentException if the object was not previously registered.
     * @throws NullPointerException     if the object is null.
     */
    fun unregister(`object`: Any?) {
        if (`object` == null) {
            throw NullPointerException("Object to unregister must not be null.")
        }
        enforcer.enforce(this)
        val producersInListener = finder.findAllProducers(`object`)
        for (entry: Map.Entry<EventType?, ProducerEvent?> in producersInListener!!.entries) {
            val key = entry.key
            val producer = getProducerForEventType(key!!)
            val value = entry.value
            if (value == null || value != producer) {
                throw IllegalArgumentException(
                    ("Missing event producer for an annotated method. Is " + `object`.javaClass
                            + " registered?")
                )
            }
            producersByType.remove(key)!!.invalidate()
        }
        val subscribersInListener = finder.findAllSubscribers(`object`)
        for (entry: Map.Entry<EventType?, Set<SubscriberEvent?>?> in subscribersInListener!!.entries) {
            val currentSubscribers: MutableSet<SubscriberEvent?> =
                getSubscribersForEventType(entry.key!!)
            val eventMethodsInListener: Collection<SubscriberEvent?> = entry.value!!
            if (currentSubscribers == null || !currentSubscribers.containsAll(eventMethodsInListener)) {
                throw IllegalArgumentException(
                    ("Missing event subscriber for an annotated method. Is " + `object`.javaClass
                            + " registered?")
                )
            }
            for (subscriber: SubscriberEvent? in currentSubscribers) {
                if (eventMethodsInListener.contains(subscriber)) {
                    subscriber!!.invalidate()
                }
            }
            currentSubscribers.removeAll(eventMethodsInListener)
        }
    }

    /**
     * Posts an event to all registered subscribers.  This method will return successfully after the event has been posted to
     * all subscribers, and regardless of any exceptions thrown by subscribers.
     *
     *
     *
     * If no subscribers have been subscribed for `event`'s class, and `event` is not already a
     * [DeadEvent], it will be wrapped in a DeadEvent and reposted.
     *
     * @param event event to post.
     * @throws NullPointerException if the event is null.
     */
    fun post(event: Any?) {
        post(Tag.DEFAULT, event)
    }

    /**
     * Posts an event to all registered subscribers.  This method will return successfully after the event has been posted to
     * all subscribers, and regardless of any exceptions thrown by subscribers.
     *
     *
     *
     * If no subscribers have been subscribed for `event`'s class, and `event` is not already a
     * [DeadEvent], it will be wrapped in a DeadEvent and reposted.
     *
     * @param tag   event tag to post.
     * @param event event to post.
     * @throws NullPointerException if the event is null.
     */
    fun post(tag: String?, event: Any?) {
        if (event == null) {
            throw NullPointerException("Event to post must not be null.")
        }
        enforcer.enforce(this)
        val dispatchClasses = flattenHierarchy(event.javaClass)
        var dispatched = false
        for (clazz: Class<*>? in dispatchClasses!!) {
            val wrappers: Set<SubscriberEvent?> = getSubscribersForEventType(EventType(tag, clazz))
            if (wrappers != null && !wrappers.isEmpty()) {
                dispatched = true
                for (wrapper: SubscriberEvent? in wrappers) {
                    dispatch(event, wrapper)
                }
            }
        }
        if (!dispatched && event !is DeadEvent) {
            post(DeadEvent(this, event))
        }
    }

    /**
     * Dispatches `event` to the subscriber in `wrapper`.  This method is an appropriate override point for
     * subclasses that wish to make event delivery asynchronous.
     *
     * @param event   event to dispatch.
     * @param wrapper wrapper that will call the handle.
     */
    protected fun dispatch(event: Any?, wrapper: SubscriberEvent?) {
        if (wrapper!!.isValid) {
            wrapper.handle(event)
        }
    }

    /**
     * Retrieves the currently registered producer for `type`.  If no producer is currently registered for
     * `type`, this method will return `null`.
     *
     * @param type type of producer to retrieve.
     * @return currently registered producer, or `null`.
     */
    fun getProducerForEventType(type: EventType): ProducerEvent? {
        return producersByType[type]
    }

    /**
     * Retrieves a mutable set of the currently registered subscribers for `type`.  If no subscribers are currently
     * registered for `type`, this method may either return `null` or an empty set.
     *
     * @param type type of subscribers to retrieve.
     * @return currently registered subscribers, or `null`.
     */
    fun getSubscribersForEventType(type: EventType): MutableSet<SubscriberEvent?> {
        return (subscribersByType[type])!!
    }

    /**
     * Flattens a class's type hierarchy into a set of Class objects.  The set will include all superclasses
     * (transitively), and all interfaces implemented by these superclasses.
     *
     * @param concreteClass class whose type hierarchy will be retrieved.
     * @return `concreteClass`'s complete type hierarchy, flattened and uniqued.
     */
    fun flattenHierarchy(concreteClass: Class<*>): Set<Class<*>>? {
        var classes = flattenHierarchyCache[concreteClass]
        if (classes == null) {
            val classesCreation = getClassesFor(concreteClass)
            classes = flattenHierarchyCache.putIfAbsent(concreteClass, classesCreation)
            if (classes == null) {
                classes = classesCreation
            }
        }
        return classes
    }

    private fun getClassesFor(concreteClass: Class<*>): Set<Class<*>> {
        val parents: MutableList<Class<*>> = LinkedList()
        val classes: MutableSet<Class<*>> = HashSet()
        parents.add(concreteClass)
        while (!parents.isEmpty()) {
            val clazz = parents.removeAt(0)
            classes.add(clazz)
            val parent = clazz.superclass
            if (parent != null) {
                parents.add(parent)
            }
        }
        return classes
    }

    companion object {
        val DEFAULT_IDENTIFIER = "default"
    }

    /**
     * Test constructor which allows replacing the default `Finder`.
     *
     * @param enforcer   Thread enforcer for register, unregister, and post actions.
     * @param identifier A brief name for this bus, for debugging purposes.  Should be a valid Java identifier.
     * @param finder     Used to discover event subscribers and producers when registering/unregistering an object.
     */
    init {
        identifier = identifier
        this.finder = finder
    }
}