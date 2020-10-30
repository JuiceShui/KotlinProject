package com.demo.kotlin.base.mvp

import androidx.annotation.StringRes
import io.reactivex.rxjava3.core.ObservableTransformer

interface IView {

    /**
     * 为RxJava操作绑定loading
     */
    fun <O> bindLoading(): ObservableTransformer<O, O>?

    /**
     * 显示loading
     */
    fun showLoadingIndicator()


    /**
     * 隐藏loading
     */
    fun hideLoadingIndicator()


    /**
     * 显示一个toast
     *
     * @param resId
     */
    fun showToast(@StringRes resId: Int)


    /**
     * 显示一个toast
     *
     * @param toast
     */
    fun showToast(toast: String?)

    /**
     * 在主线程执行
     *
     * @param action
     */
    fun safeRun(action: Runnable)
}