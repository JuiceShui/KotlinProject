package com.demo.kotlin.inject.component

import android.app.Application
import com.demo.kotlin.data.retrofit.RetrofitHelper
import com.demo.kotlin.inject.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getApp(): Application

    //提供retrofit的帮助类
    fun getRetrofitHelper(): RetrofitHelper
}