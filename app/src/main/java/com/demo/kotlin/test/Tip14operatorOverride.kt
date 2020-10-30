package com.demo.kotlin.test

class Tip14operatorOverride {
    val point: Point =
        Point(12, 9)
    val other: Point =
        Point(6, 6)
    val result1 = point - other
    val result2 = point + other
    val result3 = point * other
    val result4 = point / other
    fun print() {
        println("result point x: ${point.x},y ${point.y}")
        println("result other x: ${other.x},y ${other.y}")
        println("result1 x: ${result1.x},y ${result1.y}")
        println("result2 x: ${result2.x},y ${result2.y}")
        println("result3 x: ${result3.x},y ${result3.y}")
        println("result4 x: ${result4.x},y ${result4.y}")

    }
}

//重载+-*/方法
class Point(val x: Int, val y: Int) {

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    operator fun times(other: Point): Point {
        return Point(x * other.x, y * other.y)
    }

    operator fun div(other: Point): Point {
        return Point(x / other.x, y / other.y)
    }
}