package com.demo.kotlin.view.ui.home.contract

import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.data.entity.TXDouYinVideoEntity

interface HomeContract {
    interface View : ILoadView {
        fun onGetBanner(data: MutableList<TXDouYinVideoEntity>)
        fun onGetHomeList(data: MutableList<TXDouYinVideoEntity>)
    }

    interface Presenter {
        fun getHomeData()
    }
}