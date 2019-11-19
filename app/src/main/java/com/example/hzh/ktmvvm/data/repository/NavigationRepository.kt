package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Guide
import com.example.hzh.ktmvvm.data.bean.SubGuide
import com.example.hzh.ktmvvm.data.network.NavigationApi
import org.litepal.LitePal

/**
 * Create by hzh on 2019/11/12.
 */
class NavigationRepository private constructor() {

    companion object {

        fun getInstance(): NavigationRepository = HOLDER.instance
    }

    private object HOLDER {
        val instance = NavigationRepository()
    }

    private val service by lazy { App.httpClient.getService(NavigationApi::class.java) }

    /**
     * 获取导航
     */
    suspend fun getNavigation(): List<Guide> {
        // 获取缓存
        var navigationList = LitePal.where(
            "expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Guide::class.java)

        if (navigationList.isEmpty()) { // 缓存过期
            // 删除缓存
            LitePal.deleteAll(
                Guide::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )
            LitePal.deleteAll(
                SubGuide::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            // 从网络获取数据
            navigationList = service.getNavigation()
            // 缓存
            navigationList.forEach {
                it.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                it.save()
                it.articles.forEach { sub ->
                    sub.expired = it.expired
                    sub.save()
                }
            }
        } else navigationList.forEach {
            // 获取子项
            it.articles = LitePal.where("chapterId = ?", "${it.cid}")
                .find(SubGuide::class.java)
        }

        return navigationList
    }
}