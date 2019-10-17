package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Category
import retrofit2.http.GET

/**
 * Create by hzh on 2019/10/17.
 */
interface CategoryApi {

    /**
     * 知识体系数据
     */
    @GET("/tree/json")
    suspend fun getKnowledgeTree(): List<Category>

    /**
     * 微信公众号列表
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWeChatAuthors(): List<Category>

    /**
     * 项目分类
     */
    @GET("/project/tree/json")
    suspend fun getProjectTree(): List<Category>
}