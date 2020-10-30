package com.demo.kotlin.data.converter

import com.alibaba.fastjson.JSON
import com.demo.kotlin.data.bean.IResponse
import com.demo.kotlin.data.bean.Response
import okhttp3.ResponseBody
import retrofit2.Converter

class JsonResponseBodyConverter : Converter<ResponseBody, IResponse> {
    override fun convert(value: ResponseBody): IResponse? {
        //转化为response
        val data = value.string()
        val response = JSON.parseObject(data, Response::class.java)
        value.use {
            return response
        }
    }
}