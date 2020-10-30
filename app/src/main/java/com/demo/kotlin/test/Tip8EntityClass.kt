package com.demo.kotlin.test

data class Tip8EntityClass(val name: String = "", val id: Int = 0, val address: String = "")

fun print() {
    val entity = Tip8EntityClass("name", 1, "cd")
}