package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.data.network.WebsiteApi
import com.example.hzh.library.http.APIException
import com.example.hzh.library.http.NetConfig
import org.litepal.LitePal

/**
 * Create by hzh on 2019/11/8.
 */
class WebsiteRepository private constructor() {

    companion object {

        fun getInstance(): WebsiteRepository = Holder.mInstance
    }

    private object Holder {
        val mInstance = WebsiteRepository()
    }

    private val service by lazy { App.httpClient.getService(WebsiteApi::class.java) }

    /**
     * 获取收藏网站列表
     */
    suspend fun getCollectWebsite(): List<Website> = service.getCollectWebsite()

    /**
     * 收藏网站
     * @param name 收藏网站名
     * @param link 网站链接
     */
    suspend fun collectWebsite(name: String, link: String) = service.collectWebsite(name, link)

    /**
     * 编辑收藏网站
     * @param id 收藏网站的id
     * @param name 收藏网站名
     * @param link 网站链接
     */
    suspend fun editWebsite(id: Int, name: String, link: String) =
        service.editWebsite(id, name, link)

    /**
     * 删除收藏网站
     * @param id 收藏网站的id
     */
    suspend fun deleteWebsite(id: Int) = try {
        service.deleteWebsite(id)
    } catch (e: APIException) {
        if (e.code == NetConfig.CODE_NO_RESPONSE_BODY) "" // 操作成功了后台还是返回null，不是我的锅啊
        else throw e
    }

    /**
     * 获取热搜词
     */
    suspend fun getHotKey(): List<Website> = service.getHotKey()

    /**
     * 获取常用网站
     */
    suspend fun getCommonWebsite(): List<Website> {
        // 获取缓存
        var websiteList = LitePal.where(
            "expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Website::class.java)

        if (websiteList.isEmpty()) { // 缓存过期
            // 删除缓存
            LitePal.deleteAll(
                Website::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            // 从网络获取数据
            websiteList = service.getCommonWebsite()
            // 缓存
            websiteList.forEach { cate ->
                cate.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                cate.save()
            }
        }

        return websiteList
    }
}