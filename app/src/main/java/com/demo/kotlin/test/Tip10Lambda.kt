package com.demo.kotlin.test

import android.widget.TextView
import com.demo.kotlin.App

class Tip10Lambda {
    var tv: TextView = TextView(App.instance)


    fun test() {
        //传统形式
        tv.setOnClickListener(object : android.view.View.OnClickListener {
            override fun onClick(v: android.view.View?) {
                TODO("Not yet implemented")
            }
        })
        //lambda
        tv.setOnClickListener({ v ->
            {
                TODO()
            }
        })
        //lambda的参数如果没有使用可以省略，省略的时候用it来替代
        tv.setOnClickListener({
            //TODO
        })
        //lambda在参数最后一个时可以提出去
        tv.setOnClickListener() {
            //TODO
        }
        //lambda提出去之后，函数如果没有其他参数括号可以省略
        tv.setOnClickListener {
            //TODO
        }
    }

    interface onClickListener {
        fun onClick()
    }

    class View {
        var listener: onClickListener? = null
        fun setOnClickListener(listener: onClickListener) {
            //TODO
            this.listener = listener
        }

        fun doClick() {
            listener?.onClick()
        }

        fun setOnClickListener(listener: () -> Unit) {

        }
    }

    fun test2() {
        var view = View()
        view.setOnClickListener(object :
            onClickListener {
            override fun onClick() {
                TODO("Not yet implemented")
            }
        })
        view.setOnClickListener {
            //
        }
    }
}