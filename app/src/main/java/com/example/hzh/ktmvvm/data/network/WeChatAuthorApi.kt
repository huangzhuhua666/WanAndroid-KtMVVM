package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.bean.Page
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Create by hzh on 2019/9/21.
 */
interface WeChatAuthorApi {

    /**
     * 微信公众号列表
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWeChatAuthors(): List<Category>

    /**
     * 微信公众号历史文章
     */
    @GET("/wxarticle/list/{id}/{pageNo}/json")
    suspend fun getWeChatArticle(
        @Path("id") id: Int,
        @Path("pageNo") pageNo: Int
    ): Page<Article>

    /**
     * 微信公众号搜索文章
     */
    @GET("/wxarticle/list/{id}/{pageNo}/json")
    suspend fun searchWeChatArticle(
        @Path("id") id: Int,
        @Path("pageNo") pageNo: Int,
        @Query("k") keyword: String
    ): Page<Article>
}