package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Guide
import retrofit2.http.GET

/**
 * Create by hzh on 2019/11/12.
 */
interface NavigationApi {

    /**
     * 获取导航
     */
    @GET("/navi/json")
    suspend fun getNavigation(): List<Guide>
}