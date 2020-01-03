package com.example.hzh.ktmvvm.data.model

import android.content.Context
import android.util.Log
import com.example.hzh.ktmvvm.data.repository.DownloadRepository
import com.example.hzh.library.extension.createFile
import com.example.hzh.library.http.download.DownloadListener
import com.example.hzh.library.http.download.saveFile

/**
 * Create by hzh on 2019/12/20.
 */

private const val TAG = "download"

class DownloadModel {

    private val repo by lazy { DownloadRepository.getInstance() }

    suspend fun download(
        context: Context,
        url: String,
        missingFileName: String = "",
        listener: DownloadListener? = null
    ) = url.let {
        val file = context.createFile(it.substringAfterLast('/', missingFileName))
        if (file.exists()) file.delete()

        // 开始下载
        Log.d(TAG, "download start")
        listener?.onStart()
        repo.download(it).saveFile(file, listener)
    }
}