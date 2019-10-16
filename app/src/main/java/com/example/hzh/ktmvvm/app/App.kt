package com.example.hzh.ktmvvm.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.http.HttpClient
import com.example.hzh.library.http.NetConfig
import org.litepal.LitePal

/**
 * Create by hzh on 2019/08/23.
 */
class App : Application() {

    companion object {
        var context by DelegateExt.notNullSingleValue<App>()

        var isLogin = false

        var httpClient by DelegateExt.notNullSingleValue<HttpClient>()
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        isLogin = getSharedPreferences("Config", Context.MODE_PRIVATE).getBoolean("is_login", false)

        NetConfig.run {
            CODE_SUCCESS = 0
            CODE_LOGIN_EXPIRED = -1001
            BASE_URL = "https://www.wanandroid.com"
        }

        httpClient = HttpClient.getInstance(this)

        LitePal.initialize(this)
        LitePal.getDatabase()

        MultiDex.install(this)
    }
}