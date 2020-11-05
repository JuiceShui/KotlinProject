package com.demo.kotlin.data.retrofit

import androidx.core.os.BuildCompat
import com.demo.kotlin.BuildConfig
import com.demo.kotlin.data.ApiPath
import com.demo.kotlin.data.bean.RespBean
import com.demo.kotlin.utils.LogUtil
import io.reactivex.rxjava3.core.Observable

class APIHelper private constructor(server: String) : BaseAPI(server) {

    private class InstanceHolder {
        val instance = APIHelper(BuildConfig.SERVER)
    }

    companion object {
        val instance: APIHelper
            get() = InstanceHolder().instance
    }


    /**
     * 接口
     */
    fun getTest(key: String, num: String): Observable<RespBean<Any>> {
        val params = mapParams
        params["key"] = key
        params["num"] = num
        return mHelper.mRetrofitApi!![ApiPath.WXHotNews, params]
            .compose(handleResponse())

    }

    //毒鸡汤接口
    fun getSoap(): Observable<RespBean<Any>> {
        val params = mapParams
        params["key"] = BuildConfig.TXAPI_KEY
        return mHelper.mRetrofitApi!![ApiPath.DuJiTang, params]
            .compose(handleResponse())
    }
}