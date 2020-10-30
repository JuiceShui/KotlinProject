package com.demo.kotlin.data.entity

import com.demo.kotlin.utils.NoArg

@NoArg
data class WXHotNewsEntity(
    var ctime: String, var title: String, var views: String,
    var sourece: String, var url: String
)