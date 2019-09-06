package com.example.hzh.library.http.cookie

import android.content.Context
import android.text.TextUtils
import androidx.core.content.edit
import com.example.hzh.library.extension.byteArrayToHexString
import com.example.hzh.library.extension.hexStringToByteArray
import com.example.hzh.library.http.NetConfig
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.io.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashMap

/**
 * Create by hzh on 2019/08/27.
 */
class PersistentCookieStore(context: Context) {

    companion object {
        private const val COOKIES_PREFS = "Cookies_prefs"
    }

    private val cookies by lazy { HashMap<String, ConcurrentHashMap<String, Cookie>>() }

    private val cookiesPrefs by lazy {
        context.getSharedPreferences(
            COOKIES_PREFS,
            Context.MODE_PRIVATE
        )
    }

    init {
        // 从本地加载cookie到内存中
        val prefsMap = cookiesPrefs.all
        prefsMap.entries.forEach { entry ->
            TextUtils.split(entry.value as String, ",").forEach {
                cookiesPrefs.getString(it, null)?.run {
                    decodeCookie(this)?.run {
                        if (!cookies.containsKey(entry.key))
                            cookies[entry.key] = ConcurrentHashMap()
                        cookies[entry.key]!![it] = this
                    }
                }
            }
        }
    }

    private fun getCookieToken(cookie: Cookie): String = "${cookie.name()}@${cookie.domain()}"

    fun add(cookie: Cookie) {
        val name = getCookieToken(cookie)

        if (!cookies.containsKey(NetConfig.DOMAIN)) cookies[NetConfig.DOMAIN] = ConcurrentHashMap()
        cookies[NetConfig.DOMAIN]!![name] = cookie

        if (cookies.containsKey(NetConfig.DOMAIN)) cookiesPrefs.edit {
            putString(
                NetConfig.DOMAIN,
                TextUtils.join(",", cookies[NetConfig.DOMAIN]!!.keys.asIterable())
            )
            putString(name, encodeCookie(SerializableOkHttpCookies(cookie)))
        }
    }

    fun add(url: HttpUrl, cookie: Cookie) {
        val name = getCookieToken(cookie)

        if (!cookies.containsKey(url.host())) cookies[url.host()] = ConcurrentHashMap()
        cookies[url.host()]!![name] = cookie

        if (cookies.containsKey(url.host())) cookiesPrefs.edit {
            putString(url.host(), TextUtils.join(",", cookies[url.host()]!!.keys.asIterable()))
            putString(name, encodeCookie(SerializableOkHttpCookies(cookie)))
        }
    }

    fun get(url: HttpUrl): MutableList<Cookie> {
        val ret = mutableListOf<Cookie>()
        if (cookies.containsKey(url.host())) ret.addAll(cookies[url.host()]!!.values)
        return ret
    }

    fun get(): MutableList<Cookie> {
        val ret = mutableListOf<Cookie>()
        if (cookies.containsKey(NetConfig.DOMAIN)) ret.addAll(cookies[NetConfig.DOMAIN]!!.values)
        return ret
    }

    fun removeAll(): Boolean {
        cookiesPrefs.edit { clear() }
        cookies.clear()
        return true
    }

    fun remove(): Boolean {
        return if (cookies.containsKey(NetConfig.DOMAIN)) {
            cookiesPrefs.edit {
                cookies[NetConfig.DOMAIN]?.values?.forEach {
                    val name = getCookieToken(it)
                    if (cookiesPrefs.contains(name)) remove(name)
                }
                remove(NetConfig.DOMAIN)
            }
            cookies[NetConfig.DOMAIN]?.clear()
            cookies.remove(NetConfig.DOMAIN)
            true
        } else false
    }

    fun remove(url: HttpUrl, cookie: Cookie): Boolean {
        val name = getCookieToken(cookie)

        return if (cookies.containsKey(url.host()) && cookies[url.host()]!!.containsKey(name)) {
            cookies[url.host()]!!.remove(name)
            cookiesPrefs.edit {
                if (cookiesPrefs.contains(name)) remove(name)

                putString(url.host(), TextUtils.join(",", cookies[url.host()]!!.keys))
            }
            true
        } else false
    }

    private fun encodeCookie(cookie: SerializableOkHttpCookies): String? {
        val baos = ByteArrayOutputStream()
        try {
            val oos = ObjectOutputStream(baos)
            oos.writeObject(cookie)
        } catch (e: IOException) {
            return null
        }
        return baos.toByteArray().byteArrayToHexString()
    }

    private fun decodeCookie(cookieString: String): Cookie? {
        val bytes = cookieString.hexStringToByteArray()
        val bais = ByteArrayInputStream(bytes)
        return try {
            val ois = ObjectInputStream(bais)
            (ois.readObject() as SerializableOkHttpCookies).cookie
        } catch (ioe: IOException) {
            return null
        } catch (nfe: ClassNotFoundException) {
            return null
        }
    }
}