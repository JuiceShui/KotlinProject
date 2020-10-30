package com.demo.kotlin.utils.bus

import com.demo.kotlin.utils.bus.thread.ThreadEnforcer

object RxBus {
    /**
     * Instance of [Bus]
     */
    private var sBus: Bus? = null

    /**
     * Get the instance of [Bus]
     *
     * @return
     */
    @Synchronized
    fun get(): Bus? {
        if (sBus == null) {
            sBus = Bus(ThreadEnforcer.ANY)
        }
        return sBus
    }
}