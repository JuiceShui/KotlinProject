package com.demo.kotlin.data.retrofit

import com.demo.kotlin.data.ApiPath
import com.demo.kotlin.data.bean.IResponse
import com.demo.kotlin.data.bean.ReqMap
import com.demo.kotlin.data.bean.RespBean
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseAPI(private val server: String) : ApiPath {
    protected var mHelper: BaseRetrofitHelper = BaseRetrofitHelper(server)
    private var mLogFunction: LogFunction<RespBean<Any>?> = LogFunction()
    private var mRespFunction: RespFunction = RespFunction()
    private var mTransformResponseFunction: TransformResponseFunction = TransformResponseFunction()
    private var mResponseTransformer: ObservableTransformer<IResponse, RespBean<Any>>

    init {
        mResponseTransformer = ObservableTransformer {
            it.map(mTransformResponseFunction)
                .map(mLogFunction)
                .map(mRespFunction)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 将IResponse转化为RespBean
     * 同时打印信息处理错误
     */
    fun handleResponse(): ObservableTransformer<IResponse, RespBean<Any>> {
        return mResponseTransformer
    }

    protected val mapParams: MutableMap<String, String>
        get() = HashMap<String, String>()
    protected val reqMap: ReqMap
        get() = ReqMap()
}

