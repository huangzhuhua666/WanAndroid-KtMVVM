package com.example.hzh.ktmvvm.data.repository

import androidx.core.content.edit
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.User
import com.example.hzh.ktmvvm.data.network.UserApi
import com.example.hzh.library.http.APIException
import com.example.hzh.library.http.NetConfig
import org.litepal.LitePal

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

    private val service by lazy { App.httpClient.getService<UserApi>() }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    suspend fun login(username: String, password: String): User {
        return service.login(username, password).apply {
            // 保存登录信息
            App.isLogin = true
            App.configSP.edit {
                putBoolean("admin", admin)
                putString("email", email)
                putString("icon", icon)
                putInt("id", id)
                putString("nickname", nickname)
                putInt("type", type)
            }
            // 删除之前的缓存信息
            LitePal.deleteAll(Article::class.java)
        }
    }

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param rePassword 重复密码
     */
    suspend fun register(username: String, password: String, rePassword: String): User {
        return service.register(username, password, rePassword).apply {
            // 保存登录信息
            App.isLogin = true
            App.configSP.edit {
                putBoolean("admin", admin)
                putString("email", email)
                putString("icon", icon)
                putInt("id", id)
                putString("nickname", nickname)
                putInt("type", type)
            }
            // 删除之前的缓存信息
            LitePal.deleteAll(Article::class.java)
        }
    }

    /**
     * 退出登录
     */
    suspend fun logout() = try {
        service.logout()
    } catch (e: APIException) {
        if (e.code == NetConfig.CODE_NO_RESPONSE_BODY) { // 操作成功了后台还是返回null，不是我的锅啊
            // 清空登录信息
            App.isLogin = false
            App.configSP.edit { clear() }
            // 删除之前的缓存信息
            LitePal.deleteAll(Article::class.java)
        } else throw e
    }
}