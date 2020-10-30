package com.demo.kotlin.data.retrofit

import com.demo.kotlin.data.bean.IResponse
import com.demo.kotlin.data.bean.RespBean
import io.reactivex.rxjava3.functions.Function

class TransformResponseFunction : Function<IResponse, RespBean<Any>?> {
    override fun apply(t: IResponse?): RespBean<Any>? {
        return if (t?.getNewslist().isNullOrEmpty()) {
            RespBean(t?.getCode()!!, t.getMsg(), t.getData())
        } else {
            RespBean(t?.getCode()!!, t.getMsg(), t.getData(), t.getNewslist())
        }
    }
}