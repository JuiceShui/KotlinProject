package com.demo.kotlin.view.ui.common.presenter

import com.alibaba.fastjson.JSON
import com.demo.kotlin.base.BasePresenter
import com.demo.kotlin.data.entity.TXTips
import com.demo.kotlin.data.retrofit.RetrofitHelper
import com.demo.kotlin.view.ui.common.contract.SplashContract
import javax.inject.Inject

class SplashPresenter @Inject constructor(retrofitHelper: RetrofitHelper) :
    BasePresenter<SplashContract.View>(retrofitHelper), SplashContract.Presenter {
    override fun getTips(date: String) {
        val disposable = mRetrofitHelper.getAPI().getTipsADay(date)
            .subscribe({
                val data = JSON.parseArray(it.getData(), TXTips::class.java)
                if (data.isNotEmpty()) {
                    mView?.onGetTips(data[0])
                }
            }, {
                it.printStackTrace()
                mView?.onGetTips(null)
            })
        addSubscribe(disposable)
    }
}