package com.demo.kotlin.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

@SuppressLint("PrivateApi")
fun Activity.statusBarHeight(): Int {
    var height: Int = dp2px(20f) // default value

    try {
        val c = Class.forName("com.android.internal.R\$dimen")
        val obj = c.newInstance()
        val field = c.getField("status_bar_height")
        val dimenResId = field[obj].toString().toInt()
        height = resources.getDimensionPixelSize(dimenResId)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return height
}

fun Context.dp2px(dp: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2dp(px: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun Context.sp2px(sp: Float): Int {
    val fontScale: Float = resources.displayMetrics.scaledDensity
    return (sp * fontScale + 0.5f).toInt()
}

fun Context.px2sp(px: Float): Int {
    val fontScale: Float = resources.displayMetrics.scaledDensity
    return (px / fontScale + 0.5f).toInt()
}

fun Activity.screenHeight(): Int {
    val metric = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metric)
    return metric.heightPixels
}

fun Activity.screenWidth(): Int {
    val metric = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metric)
    return metric.widthPixels
}