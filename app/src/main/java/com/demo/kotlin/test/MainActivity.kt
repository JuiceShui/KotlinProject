package com.demo.kotlin.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.kotlin.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Tip1String().testString();
        //Tip1String().testString2()
        //Tip1String().testStringDollar()
        // println(Tip2Condition().getPoint('A'))
        //println(Tip2Condition().getPoint2(90))
        //println(Tip2Condition().getPoint3(66))
        /* println(
             Tip3FuncArgs().joinToStr(
                 collections = arrayListOf("a", "c", "d", "a", "c", "d", "a", "c", "d"),
                 postfix = "{"
             )
         )*/
        Tip4ExtensionFunc().testFunc();
        toast("JJJSSS");
        screenWidth
        Tip14operatorOverride().print()
    }
}