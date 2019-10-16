package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.bean.Page
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Create by hzh on 2019/9/29.
 */
interface ArticleApi {

    /**
     * 首页banner
     */
    @GET("/banner/json")
    suspend fun getBanners(): List<Banner>

    /**
     * 首页置顶文章
     */
    @GET("/article/top/json")
    suspend fun getTopArticles(): List<Article>

    /**
     * 首页文章列表
     */
    @GET("/article/list/{pageNo}/json")
    suspend fun getArticles(@Path("pageNo") pageNo: Int): Page<Article>

    /**
     * 知识体系下的文章
     */
    @GET("/article/list/{pageNo}/json")
    suspend fun getKnowledgeArticles(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int
    ): Page<Article>

    /**
     * 最新项目文章
     */
    @GET("/article/listproject/{pageNo}/json")
    suspend fun getNewProject(@Path("pageNo") pageNo: Int): Page<Article>

    /**
     * 项目文章，从1开始
     */
    @GET("/project/list/{pageNo}/json")
    suspend fun getProject(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int
    ): Page<Article>

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

    /**
     * 收藏文章列表
     */
    @GET("/lg/collect/list/{pageNo}/json")
    suspend fun getCollections(@Path("pageNo") pageNo: Int): Page<Article>

    /**
     * 收藏站内文章
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collectInner(@Path("id") id: Int): String

    /**
     * 收藏站外文章
     */
    @POST("/lg/collect/add/json")
    suspend fun collectOuter(): Article
}