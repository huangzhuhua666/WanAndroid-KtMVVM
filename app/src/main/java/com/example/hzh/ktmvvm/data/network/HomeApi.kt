package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.bean.Page
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Create by hzh on 2019/09/11.
 */
interface HomeApi {

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
}