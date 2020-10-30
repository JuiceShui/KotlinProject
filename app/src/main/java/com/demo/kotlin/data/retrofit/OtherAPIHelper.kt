package com.demo.kotlin.data.retrofit

import com.demo.kotlin.BuildConfig
import com.demo.kotlin.data.ApiPath
import com.demo.kotlin.data.bean.RespBean
import io.reactivex.rxjava3.core.Observable

class OtherAPIHelper private constructor(server: String) : BaseAPI(server) {

    private class InstanceHolder {
        val instance = OtherAPIHelper(BuildConfig.SERVER)
    }

    companion object {
        val instance: OtherAPIHelper
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
}