package com.demo.kotlin.test

import android.graphics.Color
import android.widget.TextView
import com.demo.kotlin.App

//用apply语句简化代码，在apply的大括号里可以访问类的公有属性和方法
class Tip12Apply {
    //常规模式
    fun normal() {
        val tv: TextView = TextView(App.instance)
        tv.text = "xx"
        tv.textSize = 12f
        tv.setTextColor(Color.RED)
    }

    //apply
    fun testApply() {
        TextView(App.instance).apply {
            text = "xx"
            textSize = 12f
            setTextColor(Color.RED)
        }
    }

    fun testWith() {
        with(TextView(App.instance)) {
            text = "xx"
            textSize = 12f
            setTextColor(Color.RED)
        }
    }
}