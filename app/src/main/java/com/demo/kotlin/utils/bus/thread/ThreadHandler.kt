package com.demo.kotlin.utils.bus.thread

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

interface ThreadHandler {
    val executor: Executor?
    val handler: Handler?

    companion object {
        val DEFAULT: ThreadHandler = object : ThreadHandler {
            override var executor: Executor? = null
                get() {
                    if (field == null) {
                        field = Executors.newCachedThreadPool()
                    }
                    return field
                }
            override var handler: Handler? = null
                get() {
                    if (field == null) {
                        field = Handler(Looper.getMainLooper())
                    }
                    return field
                }
        }
    }
}