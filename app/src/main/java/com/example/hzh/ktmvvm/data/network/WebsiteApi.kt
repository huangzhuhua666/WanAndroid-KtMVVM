package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Website
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Create by hzh on 2019/11/8.
 */
interface WebsiteApi {

    /**
     * 获取收藏网站列表
     */
    @GET("/lg/collect/usertools/json")
    suspend fun getCollectWebsite(): List<Website>

    /**
     * 收藏网站
     * @param name 收藏网站名
     * @param link 网站链接
     */
    @POST("/lg/collect/addtool/json")
    @FormUrlEncoded
    suspend fun collectWebsite(
        @Field("name") name: String,
        @Field("link") link: String
    ): Website

    /**
     * 编辑收藏网站
     * @param id 收藏网站的id
     * @param name 收藏网站名
     * @param link 网站链接
     */
    @POST("/lg/collect/updatetool/json")
    @FormUrlEncoded
    suspend fun editWebsite(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("link") link: String
    ): Website

    /**
     * 删除收藏网站
     * @param id 收藏网站的id
     */
    @POST("/lg/collect/deletetool/json")
    @FormUrlEncoded
    suspend fun deleteWebsite(@Field("id") id: Int): String
}