package com.example.hzh.ktmvvm.data.model

import com.example.hzh.ktmvvm.data.repository.DownloadRepository

/**
 * Create by hzh on 2019/12/20.
 */
class DownloadModel {

    private val repo by lazy { DownloadRepository.getInstance() }

    suspend fun download(url: String, missingFileName: String = "") = url.let {
//        downloadFile(repo.download(it), it.substringAfterLast('/', missingFileName))
    }
}