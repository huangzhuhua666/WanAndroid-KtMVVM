package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.bean.Page
import retrofit2.http.*

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
     * @param pageNo 页码从0开始
     */
    @GET("/article/list/{pageNo}/json")
    suspend fun getArticles(@Path("pageNo") pageNo: Int): Page<Article>

    /**
     * 知识体系下的文章
     * @param pageNo 页码从0开始
     * @param cid 知识体系分类
     */
    @GET("/article/list/{pageNo}/json")
    suspend fun getKnowledgeArticles(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int
    ): Page<Article>

    /**
     * 最新项目文章
     * @param pageNo 页码从0开始
     */
    @GET("/article/listproject/{pageNo}/json")
    suspend fun getNewProject(@Path("pageNo") pageNo: Int): Page<Article>

    /**
     * 项目分类文章
     * @param pageNo 页码从1开始
     * @param cid 项目分类id
     */
    @GET("/project/list/{pageNo}/json")
    suspend fun getProject(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int
    ): Page<Article>

    /**
     * 微信公众号历史文章
     * @param id 公众号id
     * @param pageNo 页码从1开始
     */
    @GET("/wxarticle/list/{id}/{pageNo}/json")
    suspend fun getWeChatArticle(
        @Path("id") id: Int,
        @Path("pageNo") pageNo: Int
    ): Page<Article>

    /**
     * 微信公众号搜索文章
     * @param id 公众号id
     * @param pageNo 页码从1开始
     */
    @GET("/wxarticle/list/{id}/{pageNo}/json")
    suspend fun searchWeChatArticle(
        @Path("id") id: Int,
        @Path("pageNo") pageNo: Int,
        @Query("k") keyword: String
    ): Page<Article>

    /**
     * 收藏文章列表
     * @param pageNo 页码从0开始
     */
    @GET("/lg/collect/list/{pageNo}/json")
    suspend fun getCollections(@Path("pageNo") pageNo: Int): Page<Article>

    /**
     * 收藏站内文章
     * @param id 文章id
     */
    @POST("/lg/collect/{id}/json")
    suspend fun collectInner(@Path("id") id: Int): String

    /**
     * 收藏站外文章
     */
    @POST("/lg/collect/add/json")
    suspend fun collectOuter(): Article

    /**
     * 文章列表取消收藏
     * @param id 文章id
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollectArticleList(@Path("id") id: Int): String

    /**
     * 我的收藏页面取消收藏
     * @param id 文章在列表中的id
     * @param originId 文章原本的id。收藏的站外文章originId为-1
     */
    @POST("/lg/uncollect/{id}/json")
    @FormUrlEncoded
    suspend fun unCollectMyCollection(
        @Path("id") id: Int,
        @Field("originId") originId: Int
    ): String
}