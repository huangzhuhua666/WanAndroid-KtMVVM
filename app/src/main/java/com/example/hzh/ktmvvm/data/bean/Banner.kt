package com.example.hzh.ktmvvm.data.bean

import com.alibaba.fastjson.annotation.JSONField
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

/**
 * Create by hzh on 2019/09/11.
 */
class Banner : LitePalSupport() {

    var desc = ""

    @JSONField(name = "id")
    @Column(unique = true)
    var bannerId = -1

    var imagePath = ""
    var isVisible = -1
    var order = -1
    var title = ""
    var type = -1
    var url = ""
    var expired = -1L
}