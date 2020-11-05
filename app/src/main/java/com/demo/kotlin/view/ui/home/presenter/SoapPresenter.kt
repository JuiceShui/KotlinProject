package com.demo.kotlin.view.ui.home.presenter

import com.alibaba.fastjson.JSON
import com.demo.kotlin.base.BasePresenter
import com.demo.kotlin.data.entity.SoapEntity
import com.demo.kotlin.data.retrofit.RetrofitHelper
import com.demo.kotlin.view.ui.home.contract.SoapContract
import javax.inject.Inject

class SoapPresenter @Inject constructor(retrofitHelper: RetrofitHelper) :
    BasePresenter<SoapContract.View>(
        retrofitHelper
    ), SoapContract.Presenter {
    override fun getSoapData() {
        val disposable = mRetrofitHelper.getAPI().getSoap()
            .subscribe({
                val data = JSON.parseArray(it.getNewslist(), SoapEntity::class.java)
                mView?.onGetSoap(data[0])
            }, {
                it.printStackTrace()
            })
        addSubscribe(disposable)
    }
}