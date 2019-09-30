package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Page
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Create by hzh on 2019/9/25.
 */
interface CollectApi {

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