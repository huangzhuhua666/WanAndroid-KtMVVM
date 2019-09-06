package com.example.hzh.library.http.cookie

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Create by hzh on 2019/09/05.
 */
class MyCookie(context: Context) : CookieJar {

    private val cookieManager by lazy { PersistentCookieStore(context) }

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        cookies.partition { it.expiresAt() < System.currentTimeMillis() }.run {
            first.forEach { cookieManager.remove(url, it) } // 移除已过期的cookie
            second.forEach { cookieManager.add(url, it) } // 保存未过期的cookie
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return cookieManager.get(url).partition { it.expiresAt() < System.currentTimeMillis() }
            .run {
                first.forEach { cookieManager.remove(url, it) } // 移除已过期的cookie
                second.filter { it.matches(url) } as MutableList // 返回匹配url的未过期的cookie
            }
    }
}