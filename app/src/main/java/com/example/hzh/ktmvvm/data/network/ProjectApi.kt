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
interface ProjectApi {

    /**
     * 项目分类
     */
    @GET("/project/tree/json")
    suspend fun getProjectTree(): List<Category>

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
}