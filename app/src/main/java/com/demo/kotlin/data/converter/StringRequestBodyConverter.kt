package com.demo.kotlin.data.converter

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.OutputStreamWriter
import java.nio.charset.Charset

class StringRequestBodyConverter : Converter<String, RequestBody> {
    companion object {
        val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
        val UTF8 = Charset.forName("utf-8")
    }

    override fun convert(value: String): RequestBody? {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF8)
        writer.use {
            writer.write(value)
        }
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }
}