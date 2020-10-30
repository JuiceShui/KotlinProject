package com.demo.kotlin.utils.bus.entity

class EventType(tag: String?, clazz: Class<*>?) {
    /**
     * Event Tag
     */
    private val tag: String

    /**
     * Event Clazz
     */
    private val clazz: Class<*>

    /**
     * Object hash code.
     */
    private val hashCode: Int
    override fun toString(): String {
        return "[EventType $tag && $clazz]"
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
        val other = obj as EventType
        return tag == other.tag && clazz == other.clazz
    }

    init {
        if (tag == null) {
            throw NullPointerException("EventType Tag cannot be null.")
        }
        if (clazz == null) {
            throw NullPointerException("EventType Clazz cannot be null.")
        }
        this.tag = tag
        this.clazz = clazz

        // Compute hash code eagerly since we know it will be used frequently and we cannot estimate the runtime of the
        // target's hashCode call.
        val prime = 31
        hashCode = (prime + tag.hashCode()) * prime + clazz.hashCode()
    }
}