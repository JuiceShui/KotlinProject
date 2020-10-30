package com.demo.kotlin.data.retrofit

class RetrofitHelper private constructor() {

    fun getAPI(): APIHelper {
        return APIHelper.instance
    }

    fun getOtherAPI(): OtherAPIHelper {
        return OtherAPIHelper.instance
    }

    companion object {
        val instance = RetrofitHelper()
    }
}