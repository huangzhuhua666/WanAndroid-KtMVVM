package com.example.hzh.ktmvvm.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.multidex.MultiDex
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.http.HttpClient
import com.example.hzh.library.http.NetConfig
import com.jeremyliao.liveeventbus.LiveEventBus
import org.litepal.LitePal

/**
 * Create by hzh on 2019/08/23.
 */
class App : Application() {

    companion object {
        var context by DelegateExt.notNullSingleValue<App>()

        var isLogin = false
            set(value) {
                field = value
                configSP.edit { putBoolean("is_login", value) }
            }
            get() = configSP.getBoolean("is_login", false)

        var httpClient by DelegateExt.notNullSingleValue<HttpClient>()

        val configSP: SharedPreferences by lazy {
            context.getSharedPreferences(
                "Config",
                Context.MODE_PRIVATE
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        NetConfig.run {
            CODE_SUCCESS = 0
            CODE_LOGIN_EXPIRED = -1001
            BASE_URL = "https://www.wanandroid.com"
        }

        httpClient = HttpClient.getInstance(this)

        LitePal.initialize(this)
        LitePal.getDatabase()

        LiveEventBus.config().autoClear(true)

        MultiDex.install(this)
    }
}