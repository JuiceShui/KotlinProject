package com.demo.kotlin.view.ui.home.contract

import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.data.entity.SoapEntity

interface SoapContract {
    interface View : ILoadView {
        fun onGetSoap(data: SoapEntity)
    }

    interface Presenter {
        fun getSoapData()
    }
}