package com.demo.kotlin.test

import android.widget.TextView

class Tip13NullPointer {
    var view: TextView? = null
    fun testLet() {
        //使用view?.let后，只有view不为空才会执行，要加 ?
        view?.let {
            it.textSize = 14f
            it.text = "xxx"
        }
    }
}