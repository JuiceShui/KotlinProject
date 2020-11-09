package com.demo.kotlin.view.ui.home.contract

import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.data.entity.WXHotNewsEntity

interface HomeAcContract {
    interface View : ILoadView {
        fun onGetData(data: MutableList<WXHotNewsEntity>)
    }

    interface Presenter {
        fun getData(num: Int)
    }
}