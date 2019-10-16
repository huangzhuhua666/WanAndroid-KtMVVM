package com.example.hzh.ktmvvm.data.bean

/**
 * Create by hzh on 2019/09/12.
 */
data class Page<T>(
    var curPage: Int,
    var datas: List<T>,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)