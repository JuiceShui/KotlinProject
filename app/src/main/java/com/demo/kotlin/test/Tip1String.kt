package com.demo.kotlin.test

class Tip1String {
    fun testString() {
        val str1 = "abc";
        val str2 = """line \n
            line2
            line3
        """
        val str3 = """
            function myFun(){
            document.getElementById("demo").innerHTML="My js";
            }
        """.trimIndent()
        println(str1)
        println(str2)
        println(str3)
    }

    fun testString2() {
        val strings = arrayListOf("a", "b", "c")
        println("total comment is $strings")
        println("first comment is ${strings[0]}")
        println("calc ${if (strings.size>0)strings[0] else "null"}")
    }

    fun testStringDollar(){
        println("this is \$string")
        println("this is ${"$"}string22")
        println("this is ${'$'}string111")

    }
}