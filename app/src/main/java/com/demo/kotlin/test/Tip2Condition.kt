package com.demo.kotlin.test

class Tip2Condition {
    fun max(x: Int, y: Int): Int {
        return if (x > y) x else y
    }

    fun max2(x: Int, y: Int) = if (x > y) x else y

    fun getPoint(grade: Char): String = when (grade) {
        'A' ->
            "Good"
        'B' ->
            "OK"
        'C' ->
            "BAD"
        else ->
            "UNKOWN"
    }

    fun getPoint2(grade: Int): String = when {
        grade > 90 -> "GOOD"
        grade > 60 -> "OK"
        grade < 60 -> "BAD"
        else -> "UNKONW"
    }

    fun getPoint3(grade: Int): String {
        return when {
            grade > 90 -> "GOOD"
            grade > 60 -> "OK"
            grade < 60 -> "BAD"
            else -> "UNKONW"
        }
    }
}