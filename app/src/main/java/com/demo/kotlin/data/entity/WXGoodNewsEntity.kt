package com.demo.kotlin.data.entity


import com.alibaba.fastjson.annotation.JSONField
import com.demo.kotlin.utils.NoArg

@NoArg
data class WXGoodNewsEntity(
    @JSONField(name = "ctime")
    val ctime: String,
    @JSONField(name = "description")
    val description: String,
    @JSONField(name = "picUrl")
    val picUrl: String,
    @JSONField(name = "title")
    val title: String,
    @JSONField(name = "url")
    val url: String
)