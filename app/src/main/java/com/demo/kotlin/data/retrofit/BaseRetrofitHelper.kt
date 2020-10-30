package com.demo.kotlin.data.retrofit

import com.demo.kotlin.data.converter.JsonConverterFactory
import com.demo.kotlin.data.converter.StringConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.util.concurrent.TimeUnit

class BaseRetrofitHelper(var server: String) {
    private var mOkHttpClient: OkHttpClient
    private val mJsonConverterFactory = JsonConverterFactory.create()
    private val mStringConverterFactory = StringConverterFactory.create()

    public var mPublicRetrofitApi: PublicRetrofitApi? = null
        get() {
            if (field == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(server)
                    .client(mOkHttpClient)
                    .addConverterFactory(mStringConverterFactory)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                field = retrofit.create(PublicRetrofitApi::class.java)
            }
            return field
        }

    public var mRetrofitApi: RetrofitApi? = null
        get() {
            if (field == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(server)
                    .client(mOkHttpClient)
                    .addConverterFactory(mJsonConverterFactory)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()
                field = retrofit.create(RetrofitApi::class.java)
            }
            return field
        }

    init {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        mOkHttpClient = builder.build()
    }
}