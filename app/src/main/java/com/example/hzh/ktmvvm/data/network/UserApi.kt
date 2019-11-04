package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Create by hzh on 2019/09/11.
 */
interface UserApi {

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): User

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param rePassword 重复密码
     */
    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): User

    /**
     * 退出登录
     */
    @GET("/user/logout/json")
    suspend fun logout(): String
}