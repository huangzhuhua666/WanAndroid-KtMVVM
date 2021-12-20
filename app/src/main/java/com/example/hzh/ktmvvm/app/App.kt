package com.example.hzh.ktmvvm.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.multidex.MultiDex
import coil.Coil
import coil.ImageLoader
import com.example.hzh.ktmvvm.util.CrashHandler
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.http.HttpClient
import com.example.hzh.library.http.HttpsUtils
import com.example.hzh.library.http.NetConfig
import com.jeremyliao.liveeventbus.LiveEventBus
import okhttp3.OkHttpClient
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

        // 异常收集
        CrashHandler.getInstance().init(this)

        // 网络配置
        NetConfig.run {
            CODE_SUCCESS = 0
            CODE_LOGIN_EXPIRED = -1001
            BASE_URL = "https://www.wanandroid.com"
        }

        httpClient = HttpClient.getInstance(this)

        initCoil()

        // 初始化LitePal
        LitePal.initialize(this)
        LitePal.getDatabase()

        // 初始化LiveEventBus
        LiveEventBus.config().autoClear(true)

        MultiDex.install(this)
    }

    private fun initCoil() {
        val builder = ImageLoader.Builder(this).run {
            okHttpClient {
                OkHttpClient.Builder().let {
                    it.cache(null)
                    it.hostnameVerifier { _, _ -> true }
                    HttpsUtils.getSSLSocketFactory()
                        ?.run { it.sslSocketFactory(sslSocketFactory, trustManager) }
                    it.build()
                }
            }
            build()
        }
        Coil.setImageLoader(builder)
    }
}