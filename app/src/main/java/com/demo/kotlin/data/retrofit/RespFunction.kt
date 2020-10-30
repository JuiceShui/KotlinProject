package com.demo.kotlin.data.retrofit

import com.demo.kotlin.data.bean.RespBean
import com.demo.kotlin.data.exception.ApiException
import io.reactivex.rxjava3.functions.Function

class RespFunction : Function<RespBean<Any>?, RespBean<Any>?> {
    override fun apply(t: RespBean<Any>?): RespBean<Any>? {
        if (t?.isException!!) {
            throw ApiException("code not correct")
        }
        return t
    }
}
