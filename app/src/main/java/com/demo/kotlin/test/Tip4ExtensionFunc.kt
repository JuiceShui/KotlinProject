package com.demo.kotlin.test

import com.demo.kotlin.test.Tip3FuncArgs

class Tip4ExtensionFunc {

    fun String.lastChar(): Char = this.get(this.length - 1)
    fun Tip3FuncArgs.lastChar(): Char = this.joinToStr(arrayListOf("a,b,c")).lastChar();
    fun testFunc() {
        val string = "xxxbbb";
        println(string.lastChar());
        println(Tip3FuncArgs().lastChar())
    }
}