package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.ktmvvm.data.model.PageBean
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Create by hzh on 2019/9/21.
 */
interface WeChatAuthorApi {

    /**
     * 微信公众号列表
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWeChatAuthors(): List<CategoryBean>

    /**
     * 微信公众号历史文章
     */
    @GET("/wxarticle/list/{id}/{pageNo}/json")
    suspend fun getWeChatArticle(
        @Path("id") id: Int,
        @Path("pageNo") pageNo: Int
    ): PageBean<ArticleBean>
}