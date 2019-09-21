package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.ktmvvm.data.model.PageBean
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
    suspend fun getProjectTree(): List<CategoryBean>

    /**
     * 最新项目文章
     */
    @GET("/article/listproject/{pageNo}/json")
    suspend fun getNewProject(@Path("pageNo") pageNo: Int): PageBean<ArticleBean>

    /**
     * 项目文章，从1开始
     */
    @GET("/project/list/{pageNo}/json")
    suspend fun getProject(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int
    ): PageBean<ArticleBean>
}