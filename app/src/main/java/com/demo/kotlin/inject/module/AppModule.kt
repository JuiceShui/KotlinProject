package com.demo.kotlin.inject.module

import android.app.Application
import com.demo.kotlin.data.retrofit.RetrofitHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var app: Application) {
    var mApp: Application = app

    @Provides
    @Singleton
    fun provideApp(): Application {
        return mApp
    }

    @Provides
    @Singleton
    fun provideRetrofitHelper(): RetrofitHelper {
        return RetrofitHelper.instance
    }
}