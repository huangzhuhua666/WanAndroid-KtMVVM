package com.example.hzh.ktmvvm.data.model

import java.io.Serializable

/**
 * Create by hzh on 2019/09/17.
 */
data class CategoryBean(
    val children: List<CategoryBean>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Serializable