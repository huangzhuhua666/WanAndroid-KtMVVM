package com.example.hzh.ktmvvm.data.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Create by hzh on 2019/12/20.
 */
interface DownloadApi {

    @Streaming
    @GET
    suspend fun download(@Url url: String): ResponseBody
}