package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.data.network.WebsiteApi
import com.example.hzh.library.http.APIException
import com.example.hzh.library.http.NetConfig

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
}