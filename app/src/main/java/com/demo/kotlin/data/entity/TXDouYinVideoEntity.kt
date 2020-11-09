package com.demo.kotlin.data.entity

import com.demo.kotlin.utils.NoArg

/**
hotindex	int	46438239	热榜指数
createtime	int	1575271793	发布时间戳
duration	int	292015	视频时长秒数
playaddr	string	https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200fe00000bnibokhum7lnrb46ns0g&line=0&ratio=540p&media_type=4&vr_type=0&improve_bitrate=0&is_play_url=1	播放地址
coverurl	string	https://p9-dy.byteimg.com/img/tos-cn-p-0015/8f5f39c794804218af85bcfecfa676bf_1575271860~c5_300x400.jpeg?from=2563711402_large	视频封面
title	string	我很庆幸我有一双健全的手，可以做我想做的事，2019充实，激动，感叹#改变生活	视频标题简介
shareurl	string	https://www.iesdouyin.com/share/video/6765740644339174669/?region=&mid=6765693082286541581&u_code=0&titleType=title	抖音分享地址
author	string	村生莫	作者抖音昵称
signature	string	山里有村，村里有莫。	作者抖音个人签名
avatar	string	https://p9-dy.byteimg.com/aweme/100x100/2e19c000644305538b6ab.jpeg	作者抖音头像
 */
@NoArg
data class TXDouYinVideoEntity(
    val author: String,
    val avatar: String,
    val coverurl: String,
    val createtime: Int,
    val duration: Int,
    val hotindex: Int,
    val playaddr: String,
    val shareurl: String,
    val signature: String,
    val title: String
)