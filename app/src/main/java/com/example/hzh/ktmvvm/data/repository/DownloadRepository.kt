package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.network.DownloadApi
import okhttp3.ResponseBody

/**
 * Create by hzh on 2019/12/20.
 */
class DownloadRepository private constructor() {

    companion object {

        fun getInstance(): DownloadRepository = Holder.mInstance
    }

    private object Holder {
        val mInstance = DownloadRepository()
    }

    private val service by lazy { App.httpClient.getService(DownloadApi::class.java) }

    suspend fun download(url: String): ResponseBody = try {
        service.download(url)
    } catch (e: Exception) {
        throw e
    }
}