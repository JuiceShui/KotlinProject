package com.demo.kotlin.utils.bus.entity

import com.demo.kotlin.utils.bus.thread.EventThread
import io.reactivex.rxjava3.core.Observable
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Wraps a 'producer' method on a specific object.
 *
 *
 *
 *  This class only verifies the suitability of the method and event type if something fails.  Callers are expected
 * to verify their uses of this class.
 */
class ProducerEvent(target: Any?, method: Method?, thread: EventThread) :
    Event() {
    /**
     * Object sporting the producer method.
     */
    val target: Any

    /**
     * Producer method.
     */
    private val method: Method

    /**
     * Producer thread
     */
    private val thread: EventThread

    /**
     * Object hash code.
     */
    private val hashCode: Int

    /**
     * Should this producer produce events
     */
    var isValid = true
        private set

    /**
     * If invalidated, will subsequently refuse to produce events.
     *
     *
     * Should be called when the wrapped object is unregistered from the Bus.
     */
    fun invalidate() {
        isValid = false
    }

    /**
     * Invokes the wrapped producer method and produce a [Observable].
     */
    fun produce(): Observable<*> {
        return Observable.create<Any> { emitter ->
            try {
                emitter.onNext(produceEvent())
                emitter.onComplete()
            } catch (e: InvocationTargetException) {
                throwRuntimeException("Producer " + this@ProducerEvent + " threw an exception.", e)
            }
        }.subscribeOn(EventThread.getScheduler(thread))
    }

    /**
     * Invokes the wrapped producer method.
     *
     * @throws IllegalStateException     if previously invalidated.
     * @throws InvocationTargetException if the wrapped method throws any [Throwable] that is not
     * an [Error] (`Error`s are propagated as-is).
     */
    @Throws(InvocationTargetException::class)
    private fun produceEvent(): Any {
        check(isValid) { toString() + " has been invalidated and can no longer produce events." }
        return try {
            method.invoke(target)
        } catch (e: IllegalAccessException) {
            throw AssertionError(e)
        } catch (e: InvocationTargetException) {
            if (e.cause is Error) {
                throw (e.cause as Error?)!!
            }
            throw e
        }
    }

    override fun toString(): String {
        return "[EventProducer $method]"
    }

    override fun hashCode(): Int {
        return hashCode
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as ProducerEvent
        return method == other.method && target === other.target
    }

    init {
        if (target == null) {
            throw NullPointerException("EventProducer target cannot be null.")
        }
        if (method == null) {
            throw NullPointerException("EventProducer method cannot be null.")
        }
        this.target = target
        this.thread = thread
        this.method = method
        method.isAccessible = true

        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
        val prime = 31
        hashCode = (prime + method.hashCode()) * prime + target.hashCode()
    }
}
