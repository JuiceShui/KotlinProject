package com.demo.kotlin.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.kotlin.R
import kotlinx.android.synthetic.main.activity_main.*

//使用apply plugin: 'kotlin-android-extensions' 自动添加view的实例对象
//fragment同理
class Tip6AutoFindView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // tv_test.text = "hhhhh";
    }
}