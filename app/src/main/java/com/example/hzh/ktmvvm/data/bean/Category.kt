package com.example.hzh.ktmvvm.data.bean

import java.io.Serializable

/**
 * Create by hzh on 2019/09/17.
 */
data class Category(
    var children: List<Category> = listOf(),
    var courseId: Int = -1,
    var id: Int = -1,
    var name: String = "",
    var order: Int = -1,
    var parentChapterId: Int = -1,
    var userControlSetTop: Boolean = false,
    var visible: Int = -1
) : Serializable