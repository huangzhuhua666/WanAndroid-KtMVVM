package com.example.hzh.library.http

import android.util.Log
import com.example.hzh.library.extension.toByteArray
import okhttp3.*
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Create by hzh on 2019/09/06.
 */
private const val TAG = "HttpClient"

private val UTF8 = Charsets.UTF_8

class VerifyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        chain.run {
            val request = request().newBuilder().run {
                addHeader("Content-Type", "application/json;charset=utf-8")
                addHeader(
                    "Accept-Language",
                    "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6,zh-CN;q=0.5,zh-HK;q=0.4"
                )
                addHeader("User-Agent", "Mozilla/5.0 (compatible; mobile; android;)")
                url(request().url())
                build()
            }

            logRequest(request)

            val start = System.nanoTime()

            val response: Response
            try {
                response = proceed(request)
            } catch (e: Exception) {
                Log.d(TAG, "<-- Http Failed: $e")
                throw e
            }

            val end = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)

            return response.logResponse(end)
        }
    }
}

private fun Interceptor.Chain.logRequest(request: Request) {
    val protocol = if (connection() != null) connection()!!.protocol() else Protocol.HTTP_1_1

    request.run {
        Log.d(TAG, "--> ${method()} ${url()} $protocol")

        body()?.run {
            if (contentType() != null) Log.d(TAG, "\tContent-Type: ${contentType()}")

            if (contentLength() != -1L) Log.d(TAG, "\tContent-Length: ${contentLength()}")
        }

        headers().names().forEach {
            if (!it.equals("Content-Type", true)
                && !it.equals("Content-Length", true)
            ) Log.d(TAG, "\t$it: ${request.header(it)}")
        }

        Log.d(TAG, " ")

        body()?.run {
            if (isReadableText(contentType())) bodyToString()
            else Log.d(TAG, "\tbody: maybe [binary body], omitted!")
        }

        Log.d(TAG, "--> End ${method()}")
    }
}

private fun Response.logResponse(costTime: Long): Response {
    newBuilder().build().run {
        try {
            Log.d(TAG, "<-- ${code()} ${message()} ${request().url()} ${costTime}ms")

            headers().names().forEach { Log.d(TAG, "\t$it: ${header(it)}") }

            Log.d(TAG, " ")

            if (HttpHeaders.hasBody(this)) {
                if (body() == null) return this

                if (isReadableText(body()!!.contentType())) {
                    val bytes = body()!!.byteStream().toByteArray()

                    Log.d(TAG, "\tbody: ${String(bytes, UTF8)}")

                    val newBody = ResponseBody.create(body()!!.contentType(), bytes)
                    return newBuilder().body(newBody).build()
                } else Log.d(TAG, "\tbody: maybe [binary body], omitted!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            Log.d(TAG, "<-- End Http")
        }
    }
    return this
}

private fun isReadableText(mediaType: MediaType?): Boolean {
    if (mediaType == null) return false

    if (mediaType.type() == "text") return true

    return mediaType.subtype().toLowerCase(Locale.US).run {
        contains("x-www-form-urlencoded") || contains("json") || contains("xml") || contains("html")
    }
}

private fun Request.bodyToString() {
    try {
        newBuilder().build().body()?.run {
            val buffer = Buffer()
            writeTo(buffer)
            Log.d(TAG, "\tbody: ${buffer.readString(UTF8)}")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}