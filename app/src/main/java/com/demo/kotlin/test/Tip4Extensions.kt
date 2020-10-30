package com.demo.kotlin.test

import android.app.Activity
import android.content.Context
import android.widget.Toast

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels
