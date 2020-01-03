package com.example.hzh.library.http.download

import java.io.File

/**
 * Create by hzh on 2020/1/3.
 */
interface DownloadListener {

    /**
     * 下载开始
     */
    suspend fun onStart() {}

    /**
     * 下载进度
     * @param progress 进度
     */
    suspend fun onProgress(progress: Double) {}

    /**
     * 下载成功
     */
    suspend fun onSuccess(file: File) {}

    /**
     * 下载失败
     */
    suspend fun onFailed() {}

    /**
     * 取消下载
     */
    suspend fun onCancel() {}
}