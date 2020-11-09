package com.demo.kotlin.view.ui.common.contract

import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.data.entity.TXTips

interface SplashContract {
    interface View : ILoadView {
        fun onGetTips(tips: TXTips?)
    }

    interface Presenter {
        fun getTips(date: String)
    }
}