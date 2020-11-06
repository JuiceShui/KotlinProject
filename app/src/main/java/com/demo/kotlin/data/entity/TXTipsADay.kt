package com.demo.kotlin.data.entity

import com.demo.kotlin.utils.NoArg

@NoArg
data class TXTipsADay(
    val content: String,
    val date: String,
    val id: Int,
    val imgurl: String,
    val note: String,
    val source: String,
    val tts: String
)