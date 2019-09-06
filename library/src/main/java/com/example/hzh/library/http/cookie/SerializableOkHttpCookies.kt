package com.example.hzh.library.http.cookie

import okhttp3.Cookie
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Create by hzh on 2019/08/27.
 */
class SerializableOkHttpCookies(@Transient val cookie: Cookie) : Serializable {

    @Transient
    private var clientCookie: Cookie? = null

    fun getBestCookie(): Cookie = clientCookie ?: cookie

    private fun writeObject(oos: ObjectOutputStream) {
        try {
            oos.run {
                with(cookie) {
                    writeObject(name())
                    writeObject(value())
                    writeLong(expiresAt())
                    writeObject(path())
                    writeBoolean(secure())
                    writeBoolean(httpOnly())
                    writeObject(domain())
                    writeBoolean(hostOnly())
                    writeBoolean(persistent())
                }
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }

    private fun readObject(ooi: ObjectInputStream) {
        try {
            clientCookie = Cookie.Builder().run {
                with(ooi) {
                    name(readObject() as String)
                    value(readObject() as String)
                    expiresAt(readLong())
                    path(readObject() as String)
                    if (readBoolean()) secure() // read secure
                    if (readBoolean()) httpOnly() // read httpOnly
                    val domain = readObject() as String
                    if (readBoolean()) hostOnlyDomain(domain) // read hostOnly
                    else domain(domain)
                    readBoolean() // read persistent
                }
                build()
            }
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}