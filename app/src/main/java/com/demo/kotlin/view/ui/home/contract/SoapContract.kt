package com.demo.kotlin.view.ui.home.contract

import com.demo.kotlin.base.mvp.ILoadView
import com.demo.kotlin.data.entity.TXDrugSoapEntity

interface SoapContract {
    interface View : ILoadView {
        fun onGetSoap(data: TXDrugSoapEntity)
    }

    interface Presenter {
        fun getSoapData()
    }
}