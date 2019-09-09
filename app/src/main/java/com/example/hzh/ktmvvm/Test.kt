package com.example.hzh.ktmvvm

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Create by hzh on 2019/09/05.
 */
interface Test {

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): String

    @GET("/user/logout/json")
    suspend fun logout(): String
}