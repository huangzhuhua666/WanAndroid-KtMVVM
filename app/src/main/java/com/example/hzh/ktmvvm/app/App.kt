package com.example.hzh.ktmvvm.app

import android.app.Application
import androidx.multidex.MultiDex
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.http.HttpClient
import com.example.hzh.library.http.NetConfig

/**
 * Create by hzh on 2019/08/23.
 */
class App : Application() {

    companion object {
        var context by DelegateExt.notNullSingleValue<App>()

        var httpClient by DelegateExt.notNullSingleValue<HttpClient>()
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

        MultiDex.install(this)
    }
}