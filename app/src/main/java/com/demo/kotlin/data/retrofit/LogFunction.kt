package com.demo.kotlin.data.retrofit

import androidx.annotation.NonNull
import com.demo.kotlin.data.bean.IResponse
import com.demo.kotlin.utils.LogUtil
import io.reactivex.rxjava3.functions.Function

class LogFunction<T : IResponse?> : Function<T, T> {
    @Throws(Exception::class)
    override fun apply(@NonNull response: T): T {
        LogUtil.v(TAG, "http response " + response.toString())
        return response
    }

    companion object {
        private val TAG = LogFunction::class.java.simpleName
    }
}