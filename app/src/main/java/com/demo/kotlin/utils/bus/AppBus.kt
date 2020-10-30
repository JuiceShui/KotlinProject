package com.demo.kotlin.utils.bus

object AppBus {
    val instance: Bus
        get() = InstanceHolder.instance!!

    fun post(e: Any?) {
        instance.post(e)
    }

    fun register(e: Any?) {
        instance.register(e)
    }

    fun unregister(e: Any?) {
        instance.unregister(e)
    }

    private object InstanceHolder {
        val instance = RxBus.get()
    }
}