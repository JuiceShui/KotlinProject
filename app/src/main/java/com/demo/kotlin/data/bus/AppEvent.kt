package com.demo.kotlin.data.bus

import android.os.Bundle


class AppEvent {
    var type: AppEventType? = null
    var datas: String? = null
    var data: Bundle? = null

    constructor() {}

    @JvmOverloads
    constructor(type: AppEventType?, datas: String? = "") {
        this.type = type
        this.datas = datas
    }

    constructor(type: AppEventType?, datas: String?, data: Bundle?) {
        this.type = type
        this.datas = datas
        this.data = data
    }
}