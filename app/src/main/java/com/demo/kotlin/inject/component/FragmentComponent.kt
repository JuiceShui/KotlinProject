package com.demo.kotlin.inject.component

import android.app.Activity
import com.demo.kotlin.inject.module.FragmentModule
import com.demo.kotlin.inject.scope.FragmentScope
import dagger.Component
import org.jetbrains.annotations.Nullable

@FragmentScope
@Component(dependencies = [AppComponent::class], modules = [FragmentModule::class])
interface FragmentComponent {
    fun getActivity(): Activity
}