package com.demo.kotlin.view.ui.home.presenter

import com.alibaba.fastjson.JSON
import com.demo.kotlin.BuildConfig
import com.demo.kotlin.base.BasePresenter
import com.demo.kotlin.data.entity.WXHotNewsEntity
import com.demo.kotlin.data.retrofit.RetrofitHelper
import com.demo.kotlin.utils.LogUtil
import com.demo.kotlin.view.ui.home.contract.HomeContract
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class HomePresenter @Inject constructor(retrofitHelper: RetrofitHelper) :
    BasePresenter<HomeContract.View>(retrofitHelper), HomeContract.Presenter {

    override fun getData(num: Int) {
        val disposable1: @NonNull Disposable? =
            mRetrofitHelper.getAPI().getTest(BuildConfig.TXAPI_KEY, "10")
                .subscribe({
                    val res = JSON.parseArray(it.getNewslist(), WXHotNewsEntity::class.java)
                    mView!!.onGetData(res)
                }, {
                    it.message.let { msg ->
                        LogUtil.e("ERRRR:${msg}")
                    }
                })

        addSubscribe(disposable1)
    }

}