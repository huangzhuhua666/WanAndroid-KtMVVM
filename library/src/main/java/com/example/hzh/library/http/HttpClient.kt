package com.example.hzh.library.http

import android.content.Context
import com.example.hzh.library.http.cookie.MyCookie
import com.example.hzh.library.http.json.FastJsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Create by hzh on 2019/08/27.
 */
class HttpClient private constructor(context: Context) {

    companion object {

        private var retrofit: Retrofit? = null

        @Volatile
        private var instance: HttpClient? = null

        fun init(context: Context) =
            instance ?: synchronized(HttpClient::class.java) {
                instance ?: HttpClient(context).apply { instance = this }
            }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder().run {
            retryOnConnectionFailure(true)
            connectTimeout(NetConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NetConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NetConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)
            cookieJar(MyCookie(context))
//        cache()
            addInterceptor(VerifyInterceptor())
            HttpsUtils.getSSLSocketFactory()
                ?.run { sslSocketFactory(sslSocketFactory, trustManager) }
            hostnameVerifier { _, _ -> true }
            build()
        }
    }

    init {
        retrofit = Retrofit.Builder().run {
            baseUrl(NetConfig.BASE_URL)
            addConverterFactory(FastJsonConverterFactory.create())
            client(okHttpClient)
            build()
        }
    }

    fun <T> getService(service: Class<T>): T = retrofit!!.create(service)
}