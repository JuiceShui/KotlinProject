package com.demo.kotlin.data.converter

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Converter

class StringResponseBodyConverter : Converter<ResponseBody, String> {
    override fun convert(value: ResponseBody): String? {
        value.use {
            return value.string()
        }
    }
}