package com.demo.kotlin.utils

import android.util.Log

class LogUtil {
    companion object {
        val VERBOSE = 1
        val DEBUG = 2
        val INFO = 3
        val WARN = 4
        val ERROR = 5
        val NOTHING = 6

        private var LEVEL = VERBOSE
        private var GLOBAL_TAG: String = LogUtil::class.java.simpleName

        fun init(isDebug: Boolean, globalTag: String, logLevel: Int) {
            GLOBAL_TAG = globalTag
            LEVEL = logLevel
            if (isDebug) {

            }
        }

        fun v(msg: String) {
            v(GLOBAL_TAG, msg)
        }

        fun d(msg: String) {
            d(GLOBAL_TAG, msg)
        }

        fun i(msg: String) {
            i(GLOBAL_TAG, msg)
        }

        fun w(msg: String) {
            w(GLOBAL_TAG, msg)
        }

        fun e(msg: String) {
            e(GLOBAL_TAG, msg)
        }


        fun v(tag: String, msg: String) {
            if (LEVEL <= VERBOSE) {
                Log.v(tag, msg);
            }
        }

        fun d(tag: String, msg: String) {
            if (LEVEL <= DEBUG) {
                Log.d(tag, msg)
            }
        }

        fun i(tag: String, msg: String) {
            if (LEVEL <= INFO) {
                Log.i(tag, msg);
            }
        }

        fun w(tag: String, msg: String) {
            if (LEVEL <= WARN) {
                Log.w(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (LEVEL <= ERROR) {
                Log.e(tag, msg)
            }
        }
    }
}