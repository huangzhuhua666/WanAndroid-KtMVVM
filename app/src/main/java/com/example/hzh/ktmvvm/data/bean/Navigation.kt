package com.example.hzh.ktmvvm.data.bean

import org.litepal.crud.LitePalSupport

/**
 * Create by hzh on 2019/11/11.
 */
class Navigation : LitePalSupport() {

    val articles = listOf<SubNavigation>()
    var cid = -1
    var name = ""
    var expired = -1L
}

class SubNavigation : LitePalSupport() {

    var link = ""
    var title = ""
    var chapterId = -1
    var expired = -1L
}