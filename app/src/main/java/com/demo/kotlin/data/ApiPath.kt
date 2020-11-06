package com.demo.kotlin.data

interface ApiPath {
    companion object {

        const val CAIHONGPI_TEST = "/txapi/caihongpi/index"

        const val JIZHUANWAI_TEST = "/txapi/naowan/index"

        //微信热点新闻
        const val WXHotNews = "/txapi/wxhotarticle/index"

        //微信精选
        const val WXGoodNews = "/wxnew/index"

        //毒鸡汤
        const val TXDrugSoap = "/txapi/dujitang/index"

        //每日一句
        const val TXTipsADay = "/txapi/everyday/index"
    }
}