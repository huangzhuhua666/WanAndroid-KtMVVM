package com.example.hzh.ktmvvm.data.bean

import com.alibaba.fastjson.annotation.JSONField
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Create by hzh on 2019/09/17.
 */
class Category : LitePalSupport(), Serializable {

    var children = listOf<Category>()
    var courseId = -1

    @JSONField(name = "id")
    @Column(unique = true)
    var categoryId = -1

    var name = ""
    var order = -1
    var parentChapterId = -1
    var userControlSetTop = false
    var visible = -1
    var tag = 0
    var expired = -1L
}