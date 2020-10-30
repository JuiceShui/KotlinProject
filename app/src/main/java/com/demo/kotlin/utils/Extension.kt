package com.demo.kotlin.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

fun Activity.statusBarHeight(): Int {
    val metric = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metric)
    return metric.widthPixels
}