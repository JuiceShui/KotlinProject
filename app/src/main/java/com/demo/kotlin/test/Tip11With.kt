package com.demo.kotlin.test

import java.lang.StringBuilder

class Tip11With {
    fun addChar(): String {
        val result = StringBuilder()
        result.append("START")
        for (char in 'A'..'Z') {
            result.append(char)
        }
        result.append("END\n")
        return result.toString()
    }

    fun addChar2(): String {
        val result = StringBuilder()
        //使用with ，内部可用this，代替result
        with(result) {
            this.append("STRAT")
            for (char in 'A'..'Z') {
                this.append(char)
            }
            this.append("END\n")
        }
        return result.toString()
    }

    fun addChar3(): String {
        val result = StringBuilder()
        //使用with ，内部可用this，代替result,或者省略this
        with(result) {
            append("STRAT")
            for (char in 'A'..'Z') {
                append(char)
            }
            append("END\n")
        }
        return result.toString()
    }

    //直接省略掉result的赋值，用个新的stringBuilder代替
    fun addChar4(): String {
        return with(StringBuilder()) {
            for (char in 'A'..'Z') {
                append(char)
            }
            append("END\n")
            toString()
        }
    }
}