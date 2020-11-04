package com.demo.kotlin.inject.component

import android.app.Activity
import com.demo.kotlin.inject.module.ActivityModule
import com.demo.kotlin.inject.scope.ActivityScope
import com.demo.kotlin.view.ui.home.HomeActivity
import com.demo.kotlin.view.ui.home.MainActivity
import dagger.Component
import org.jetbrains.annotations.Nullable

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun getActivity(): Activity

    fun inject(activity: HomeActivity)

    fun inject(activity: MainActivity)
}