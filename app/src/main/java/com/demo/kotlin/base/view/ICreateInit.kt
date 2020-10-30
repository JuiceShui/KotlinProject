package com.demo.kotlin.base.view

import androidx.annotation.LayoutRes

/**
 * 界面初始化接口，activity和fragment实现此接口
 */
interface ICreateInit {
    /**
     * 获得布局xml
     */
    @get:LayoutRes
    val layoutId: Int

    /**
     * 初始化界面跳转参数
     */
    fun initParams()

    /**
     * 视图按需初始化
     */
    fun initViews()

    /**
     * 初始化绑定事件
     */
    fun initListeners()

    /**
     * 获取界面数据
     */
    fun getData()
}