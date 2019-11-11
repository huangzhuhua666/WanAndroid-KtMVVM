package com.example.hzh.ktmvvm.data.model

import android.text.TextUtils
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.data.repository.WebsiteRepository
import com.example.hzh.ktmvvm.util.OperateCallback

/**
 * Create by hzh on 2019/11/8.
 */
class WebsiteModel {

    private val repo by lazy { WebsiteRepository.getInstance() }

    /**
     * 获取收藏网站列表
     */
    suspend fun getCollectWebsite(): List<Website> = repo.getCollectWebsite()

    /**
     * 收藏网站
     * @param website
     * @param callback
     */
    suspend fun collectWebsite(website: Website, callback: OperateCallback<Website>) {
        website.run {
            if (TextUtils.isEmpty(name)) {
                callback.onInputIllegal(R.string.please_input_website_name)
                return
            }

            if (TextUtils.isEmpty(link)) {
                callback.onInputIllegal(R.string.please_input_website_link)
                return
            }

            callback.onPreOperate()

            repo.collectWebsite(name, link).let { callback.onCallback(it) }
        }
    }

    /**
     * 编辑收藏网站
     * @param website
     * @param callback
     */
    suspend fun editWebsite(website: Website, callback: OperateCallback<Website>) {
        website.run {
            if (TextUtils.isEmpty(name)) {
                callback.onInputIllegal(R.string.please_input_website_name)
                return
            }

            if (TextUtils.isEmpty(link)) {
                callback.onInputIllegal(R.string.please_input_website_link)
                return
            }

            callback.onPreOperate()

            repo.editWebsite(websiteId, name, link).let { callback.onCallback(it) }
        }
    }

    /**
     * 删除收藏网站
     * @param id 收藏网站的id
     */
    suspend fun deleteWebsite(id: Int) = repo.deleteWebsite(id)

    /**
     * 获取热搜词
     */
    suspend fun getHotKey(): List<Website> = repo.getHotKey()

    /**
     * 获取常用网站
     */
    suspend fun getCommonWebsite(): List<Website> = repo.getCommonWebsite()
}