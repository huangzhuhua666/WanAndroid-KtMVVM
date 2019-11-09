package com.example.hzh.ktmvvm.data.bean

import com.alibaba.fastjson.annotation.JSONField
import org.litepal.crud.LitePalSupport

/**
 * Create by hzh on 2019/11/8.
 */
class Website : LitePalSupport() {

    var desc: String = ""
    var icon: String = ""

    @JSONField(name = "id")
    var websiteId: Int = -1

    var link: String = ""

    var name: String = ""

    var order: Int = -1
    var userId: Int = -1
    var visible: Int = -1
}