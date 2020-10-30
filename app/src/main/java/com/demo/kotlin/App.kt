package com.demo.kotlin

import com.demo.kotlin.App.Companion.instance
import com.demo.kotlin.base.BaseApplication
import com.demo.kotlin.inject.component.AppComponent
import com.demo.kotlin.inject.component.DaggerAppComponent
import com.demo.kotlin.inject.module.AppModule

class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }


    companion object {
        //  private var instance: MyApp? = null
        @get:Synchronized
        var instance: App? = null
            get() {
                return field
            }
        var mAppComponent: AppComponent? = null

        fun getAppComponent(): AppComponent? {
            if (mAppComponent == null) {
                mAppComponent = DaggerAppComponent.builder()
                    .appModule(AppModule(instance!!))
                    .build()
            }
            return mAppComponent
        }
    }

}