package com.demo.kotlin.test

class Tip5LazyLateInit {
    //by lazy 用于修饰val
    //lateinit 修饰var ，且变量为非空
    val user: User by lazy {
        User("hh", 12)
    }
    lateinit var user2: User
    fun initUser() {
        user2 = User("xx", 13)
    }
}

class User(name: String, age: Int)