package com.example.hzh.ktmvvm.data.model

import java.io.Serializable

/**
 * Create by hzh on 2019/09/17.
 */
data class KnowledgeBean(
    val children: List<KnowledgeBean>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val visible: Int
) : Serializable