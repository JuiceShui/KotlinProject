package com.demo.kotlin.base.mvp

/**
 * MVP中的 Presenter 层接口
 * 适用于有网络处理的界面
 */
interface IPresenter<T : ILoadView> {
    /**
     * 绑定View
     * 设置View引用
     *
     * @param view
     */
    fun attachView(view: T)


    /**
     * 解除View
     * 释放View引用
     */
    fun detachView()


    /**
     * presenter初始化需要执行的操作
     */
    fun start()

    fun onViewResume()

    fun onViewPause()

    fun onViewCreate()

    fun onViewDestroy()
}