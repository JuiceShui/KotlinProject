package com.demo.kotlin.test

class Tip3FuncArgs {
    @JvmOverloads   //便于java函数调用默认参数，不加JvmOverloads的话，java调用默认参数会出错
    fun <T> joinToStr(
        collections: Collection<T>,
        separator: String = "-",
        prefix: String = "[",
        postfix: String = "]"
    ): String {
        val result = StringBuilder(prefix);
        for ((k, v) in collections.withIndex()) {
            if (k > 0) result.append(separator)
            result.append(v)
        }
        result.append(postfix)
        return result.toString()
    }
}