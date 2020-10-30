package com.demo.kotlin.base.mvp
/**
 * MVP中的 View 层接口
 * 适用于有网络处理的界面
 */
interface ILoadView:IView {

    /**
     * 展示加载中的界面
     */
    fun showLoadingView()


    /**
     * 展示加载错误的界面
     */
    fun showLoadErrorView()


    /**
     * 展示加载成功的界面
     */
    fun showMainView()


    /**
     * 处理网络请求返回的异常
     *
     * @param throwable 抛出的异常
     */
    fun handleException(throwable: Throwable?)
}