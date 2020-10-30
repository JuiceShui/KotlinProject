package com.demo.kotlin.utils

import android.content.Context

class Utils {
    private var mContext: Context? = null

    private object InstanceHolder {
        val instance: Utils =
            Utils()
    }

    private fun YYUtils() {}

    fun getInstance(): Utils? {
        return InstanceHolder.instance
    }

    fun init(context: Context) {
        mContext = context.applicationContext
    }

    fun getContext(): Context? {
        if (mContext == null) {
            throw RuntimeException("should init first!")
        }
        return mContext
    }
}