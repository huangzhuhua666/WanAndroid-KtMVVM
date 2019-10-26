package com.example.hzh.ktmvvm.data.repository

import androidx.core.content.edit
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.User
import com.example.hzh.ktmvvm.data.network.UserApi

/**
 * Create by hzh on 2019/10/22.
 */
class UserRepository private constructor() {

    companion object {

        fun getInstance(): UserRepository = Holder.instance
    }

    private object Holder {
        val instance = UserRepository()
    }

    private val service by lazy { App.httpClient.getService(UserApi::class.java) }

    suspend fun login(username: String, password: String): User {
        return service.login(username, password).apply {
            App.isLogin = true
            App.configSP.edit {
                putBoolean("admin", admin)
                putString("email", email)
                putString("icon", icon)
                putInt("id", id)
                putString("nickname", nickname)
                putInt("type", type)
            }
        }
    }

    suspend fun register(username: String, password: String, rePassword: String): User {
        return service.register(username, password, rePassword).apply {
            App.isLogin = true
            App.configSP.edit {
                putBoolean("admin", admin)
                putString("email", email)
                putString("icon", icon)
                putInt("id", id)
                putString("nickname", nickname)
                putInt("type", type)
            }
        }
    }
}