package com.demo.kotlin.data.entity

import com.demo.kotlin.utils.NoArg

/**
id	int	3673	数据ID
content	string	Solitude is the soul’s holiday......	句子内容
source	string	American drama lines	来源
note	string	独处是灵魂的假期......	释义
tts	string	https://edu-wps.ks3-cn-beijing.ksyun.com/audio/3534bca1b7ec29560daa7e1960b9bd62.mp3	音频地址
imgurl	string	https://edu-wps.ks3-cn-beijing.ksyun.com/image/a356fcff007bd65048e48e92bd6795df.png	分享图片地址
date	string	2020-02-22	时间
 */
@NoArg
data class TXTips(
    val content: String,
    val date: String,
    val id: Int,
    val imgurl: String,
    val note: String,
    val source: String,
    val tts: String
)