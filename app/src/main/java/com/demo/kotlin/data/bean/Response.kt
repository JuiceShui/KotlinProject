package com.demo.kotlin.data.bean

class Response : IResponse {

    private var code = 0
    private var msg: String? = null
    private var data: String? = null

    //TODO  其他类型数据
    private var newslist: String? = null
    fun Response() {

    }

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
}