package com.demo.kotlin.data.bus

enum class AppEventType {
    TEST,
    ON_CONNECT_REQUEST,  //接收到连接请求
    LOGIN,  //app登录
    LOGOUT
    //app退出登录
}
