package com.example.hzh.ktmvvm.event

/**
 * @author huangzhuhua
 * @date 2022/3/7
 */
sealed class ArticleModifyEvent {

    data class Edit(val id: Int, val collect: Boolean) : ArticleModifyEvent()
}