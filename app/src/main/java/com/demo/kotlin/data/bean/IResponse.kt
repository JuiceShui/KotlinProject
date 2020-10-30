package com.demo.kotlin.data.bean

interface IResponse {
    fun setCode(code: Int)

    fun setMsg(msg: String?)

    fun setData(data: String?)

    fun setNewslist(newslist: String?)

    fun getCode(): Int

    fun getMsg(): String?

    fun getData(): String?

    fun getNewslist(): String?

}