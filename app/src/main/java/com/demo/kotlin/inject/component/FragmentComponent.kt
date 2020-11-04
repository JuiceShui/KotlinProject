package com.demo.kotlin.inject.component

import android.app.Activity
import com.demo.kotlin.inject.module.FragmentModule
import com.demo.kotlin.inject.scope.FragmentScope
import com.demo.kotlin.view.ui.home.fragment.HomeFragment
import dagger.Component
import org.jetbrains.annotations.Nullable

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun getActivity(): Activity

    fun inject(fragment: HomeFragment)
}