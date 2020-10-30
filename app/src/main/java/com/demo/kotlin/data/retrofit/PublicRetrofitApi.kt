package com.demo.kotlin.data.retrofit

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface PublicRetrofitApi {
    @GET
    operator fun get(@Url url: String): Observable<String>

    @GET
    operator fun get(
        @Url url: String,
        @QueryMap params: MutableMap<String, String>
    ): Observable<String>

    @POST
    fun post(@Url url: String): Observable<String>

    @POST
    @FormUrlEncoded
    fun post(@Url url: String, @FieldMap params: MutableMap<String, String>): Observable<String>

    @Multipart
    @POST
    fun post(
        @Url url: String,
        @QueryMap params: MutableMap<String, String>,
        @PartMap multipart: MutableMap<String, RequestBody>
    ): Observable<String>

    @Streaming
    @GET
    fun download(@Url url: String): Observable<ResponseBody>
}