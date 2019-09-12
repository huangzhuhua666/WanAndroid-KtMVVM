package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.data.model.BannerBean
import com.example.hzh.ktmvvm.data.model.PageBean
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
    suspend fun getBanners(): List<BannerBean>

    /**
     * 首页置顶文章
     */
    @GET("/article/top/json")
    suspend fun getTopArticles(): List<ArticleBean>

    /**
     * 首页文章列表
     */
    @GET("/article/list/{pageNo}/json")
    suspend fun getArticles(@Path("pageNo") pageNo: Int): PageBean<ArticleBean>
}