package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.ArticleBean
import com.example.hzh.ktmvvm.data.bean.PageBean
import com.example.hzh.ktmvvm.data.bean.CategoryBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Create by hzh on 2019/09/17.
 */
interface KnowledgeApi {

    /**
     * 知识体系数据
     */
    @GET("/tree/json")
    suspend fun getKnowledgeTree(): List<CategoryBean>

    /**
     * 知识体系下的文章
     */
    @GET("/article/list/{pageNo}/json")
    suspend fun getKnowledgeArticles(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int
    ): PageBean<ArticleBean>
}