package com.demo.kotlin.inject.module

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.demo.kotlin.base.BaseFragment
import com.demo.kotlin.inject.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
open class FragmentModule(private var mFragment: Fragment) {
    @Provides
    @FragmentScope
    fun provideActivity(): Activity {
        return mFragment.activity!!
    }

    @Provides
    @FragmentScope
    fun provideFragment(): Fragment {
        return mFragment
    }

    @Provides
    @FragmentScope
    protected fun provideBinding(): ViewDataBinding {
        return DataBindingUtil.inflate(
            mFragment.activity!!.layoutInflater,
            (mFragment as BaseFragment).layoutId, null, false
        )
    }
}