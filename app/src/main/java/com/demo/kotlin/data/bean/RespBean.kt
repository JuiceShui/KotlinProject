package com.demo.kotlin.data.bean

import com.alibaba.fastjson.JSON

class RespBean<T> : IResponse {
    // 2 参数缺失
    // 3 ras接口验证失败
    // 4 时间戳过期
    // 5 token过期
    // 6 token异常
    // 7 token aes解密失败
    private var code = 0
    private var msg: String? = null
    private var data: String? = null
    private var newslist: String? = null
    var bean: T? = null
        private set

    constructor() {}
    constructor(code: Int, msg: String?, data: String?) {
        this.code = code
        this.msg = msg
        this.data = data
        data?.let { setBean(it) }
    }

    constructor(code: Int, msg: String?, data: String?, newslist: String?) {
        this.code = code
        this.msg = msg
        this.data = newslist
        this.newslist = newslist
        data?.let { setBean(it) }
    }

    /**
     * token过期
     */
    val isLoginExpire: Boolean
        get() = code == 5 || code == 1

    /**
     * token异常，一般是在其他设备登录导致
     */
    val isLoginAbnormal: Boolean
        get() = code == 7 || code == 6

    /**
     * 时间戳过期，一般是本机时间与服务器时间差距过大导致
     */
    val isTimestampExpire: Boolean
        get() = code == 4

    val isException: Boolean
        get() = code != 200

    override fun getCode(): Int {
        return code
    }

    override fun setCode(code: Int) {
        this.code = code
    }

    override fun getMsg(): String? {
        return msg
    }

    override fun setMsg(msg: String?) {
        this.msg = msg
    }

    override fun getData(): String? {
        return data
    }

    override fun getNewslist(): String? {
        return newslist
    }

    override fun setData(data: String?) {
        this.data = data
    }

    override fun setNewslist(newslist: String?) {
        this.newslist = newslist
    }

    private fun setBean(s: String) {
        try {
            bean = JSON.parse(s) as T
        } catch (ignore: Exception) {
        }
    }

    override fun toString(): String {
        return """
            {"code":$code,"msg":"$msg",
            "data":"$data"}
            """.trimIndent()
    }
}
