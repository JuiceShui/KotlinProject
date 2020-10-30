package com.demo.kotlin.utils.bus.entity

import android.util.Log
import androidx.annotation.NonNull
import com.demo.kotlin.utils.bus.thread.EventThread
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class SubscriberEvent(target: Any?, method: Method?, thread: EventThread?) :
    Event() {
    /**
     * Object sporting the method.
     */
    private val target: Any

    /**
     * Subscriber method.
     */
    private val method: Method

    /**
     * Subscriber thread
     */
    private val thread: EventThread

    /**
     * RxJava [Subject]
     */
    var subject: Subject<*>? = null
        private set

    /**
     * Object hash code.
     */
    private val hashCode: Int

    /**
     * Should this Subscriber receive events?
     */
    var isValid = true
        private set

    private fun initObservable() {
        subject = PublishSubject.create<Any>()
        subject!!.toFlowable(BackpressureStrategy.BUFFER).observeOn(EventThread.getScheduler(thread))
            .subscribe(object : Consumer<Any?> {
                @Throws(Exception::class)
                override fun accept(@NonNull event: Any?) {
                    try {
                        if (isValid) {
                            handleEvent(event)
                        }
                    } catch (e: InvocationTargetException) {
                        //modify by scz
                        //throwRuntimeException("Could not dispatch event: " + event.getClass() + " to subscriber " + SubscriberEvent.this, e);
                        Log.e(
                            SubscriberEvent::class.java.simpleName,
                            "Could not dispatch event: " + event!!.javaClass + " to subscriber " + this@SubscriberEvent + ":" + e.message
                        )
                    }
                }
            })
    }

    /**
     * If invalidated, will subsequently refuse to handle events.
     *
     *
     * Should be called when the wrapped object is unregistered from the Bus.
     */
    fun invalidate() {
        isValid = false
    }

    fun handle(event: Any?) {
        subject!!.onNext(event as Nothing?)
    }

    /**
     * Invokes the wrapped subscriber method to handle `event`.
     *
     * @param event event to handle
     * @throws IllegalStateException     if previously invalidated.
     * @throws InvocationTargetException if the wrapped method throws any [Throwable] that is not
     * an [Error] (`Error`s are propagated as-is).
     */
    @Throws(InvocationTargetException::class)
    protected fun handleEvent(event: Any?) {
        check(isValid) { toString() + " has been invalidated and can no longer handle events." }
        try {
            method.invoke(target, event)
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
        return "[SubscriberEvent $method]"
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
        val other = obj as SubscriberEvent
        return method == other.method && target === other.target
    }

    init {
        if (target == null) {
            throw NullPointerException("SubscriberEvent target cannot be null.")
        }
        if (method == null) {
            throw NullPointerException("SubscriberEvent method cannot be null.")
        }
        if (thread == null) {
            throw NullPointerException("SubscriberEvent thread cannot be null.")
        }
        this.target = target
        this.method = method
        this.thread = thread
        method.isAccessible = true
        initObservable()

        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
        val prime = 31
        hashCode = (prime + method.hashCode()) * prime + target.hashCode()
    }
}
