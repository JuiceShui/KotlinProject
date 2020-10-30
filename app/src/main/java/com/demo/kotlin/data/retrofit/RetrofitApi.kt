package com.demo.kotlin.data.retrofit

import com.demo.kotlin.data.bean.IResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitApi {
    /**
     * @param relativeUrl 请求的相对地址
     * @return 返回对应数据信息
     */
    @GET
    operator fun get(@Url relativeUrl: String): Observable<IResponse>

    @GET
    operator fun get(
        @Url relativeUrl: String,
        @QueryMap params: MutableMap<String, String>
    ): Observable<IResponse>

    @POST
    @FormUrlEncoded
    fun post(@Url relativeUrl: String): Observable<IResponse>

    @POST
    @FormUrlEncoded
    fun post(
        @Url relativeUrl: String,
        @FieldMap params: MutableMap<String, String>
    ): Observable<IResponse>

    /**
     * 上传文件
     * @param multipart 文件参数
     */
    @Multipart
    @POST
    fun post(
        @Url relativeUrl: String,
        @QueryMap params: MutableMap<String, String>,
        @PartMap multipart: MutableMap<String, RequestBody>
    ): Observable<IResponse>

}