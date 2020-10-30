package com.demo.kotlin.inject.module

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.demo.kotlin.base.BaseActivity
import com.demo.kotlin.inject.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
open class ActivityModule(var mActivity: Activity) {

    @Provides
    @ActivityScope
    fun provideActivity(): Activity {
        return mActivity
    }

    @Provides
    @ActivityScope
    protected fun provideBinding(): ViewDataBinding {
        return DataBindingUtil.setContentView(mActivity, (mActivity as BaseActivity).layoutId)
    }
}