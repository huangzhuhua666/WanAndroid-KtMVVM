package com.example.hzh.ktmvvm.data.bean

import org.litepal.crud.LitePalSupport

/**
 * Create by hzh on 2019/11/11.
 */
class Guide : LitePalSupport() {

    var articles = listOf<SubGuide>()
    var cid = -1
    var name = ""
    var expired = -1L

    fun toMap(): Map<String, Any> = mapOf("cid" to cid, "name" to name)
}

class SubGuide : LitePalSupport() {

    var link = ""
    var title = ""
    var chapterId = -1
    var expired = -1L

    fun toMap(): Map<String, Any> = mapOf(
        "link" to link,
        "title" to title,
        "chapterId" to chapterId
    )
}