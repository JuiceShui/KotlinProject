package com.demo.kotlin.base

import com.demo.kotlin.App
import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.base.mvp.IPresenter
import com.demo.kotlin.data.retrofit.RetrofitHelper
import dagger.Provides
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BasePresenter<T : ILoadView>(retrofitHelper: RetrofitHelper) :
    IPresenter<T> {
    protected var mApp: App = App.instance!!

    var mRetrofitHelper = retrofitHelper

    protected var mView: T? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    /**
     * 需要显示loading时，使用此方法处理
     */
    protected fun <O> bindLoading(): ObservableTransformer<O, O> {    //compose简化线程
        return mView?.bindLoading()!!
    }

    /**
     * 不需要显示loading标志时，使用此方法管理订阅
     *
     * @param subscription
     */
    protected fun addSubscribe(subscription: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(subscription)
    }

    /**
     * 暂不开放本方法
     */
    private fun unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.dispose()
        }
    }

    override fun start() {}
    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        // 取消订阅时，需要设置loading，因此需要在mView置空之前
        unSubscribe()
        mView = null
    }

    override fun onViewResume() {}
    override fun onViewPause() {}
    override fun onViewCreate() {}
    override fun onViewDestroy() {}
    protected fun safeRun(action: Runnable) {
        if (mView == null) {
            return
        }
        mView?.safeRun(action)
    }

    protected open fun getResourceStr(resId: Int): String? {
        return mApp.resources?.getString(resId)
    }
}