package com.demo.kotlin.view.ui.home.presenter

import com.alibaba.fastjson.JSON
import com.demo.kotlin.base.BasePresenter
import com.demo.kotlin.data.entity.TXDouYinVideoEntity
import com.demo.kotlin.data.retrofit.RetrofitHelper
import com.demo.kotlin.view.ui.home.contract.HomeContract
import javax.inject.Inject

class HomePresenter @Inject constructor(retrofitHelper: RetrofitHelper) :
    BasePresenter<HomeContract.View>(retrofitHelper), HomeContract.Presenter {
    override fun getHomeData() {
        val disposable = mRetrofitHelper.getAPI().getDouYinVideo()
            .subscribe({ bean ->
                val data = JSON.parseArray(bean.getNewslist(), TXDouYinVideoEntity::class.java)
                if (data.isNotEmpty()) {
                    if (data.size > 5) {
                        mView?.let {
                            it.onGetBanner(data.subList(0, 5))
                            it.onGetHomeList(data.subList(5, data.size))
                        }
                    }
                }
            }, {
                it.printStackTrace()
            })
        addSubscribe(disposable)
    }
}