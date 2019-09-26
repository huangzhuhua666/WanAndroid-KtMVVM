package com.example.hzh.ktmvvm.data.bean

/**
 * Create by hzh on 2019/09/12.
 */
data class PageBean<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)