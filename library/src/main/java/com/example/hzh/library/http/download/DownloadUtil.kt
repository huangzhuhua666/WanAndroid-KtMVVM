package com.example.hzh.library.http.download

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

/**
 * Create by hzh on 2019/12/21.
 */
suspend fun downloadFile(body: ResponseBody, file: File) = withContext(Dispatchers.IO) {
    launch(Dispatchers.Main) {
        // TODO star
    }

    val ins = body.byteStream()
    val fos = FileOutputStream(file)
    try {
        val totalLength = body.contentLength()
        val buff = ByteArray(1024)

        var currentLength = 0L
        var progress = 0.0

        var len = ins.read(buff)
        while (len != -1) {
            fos.write(buff, 0, len)
            currentLength += len

            if ((currentLength.toDouble() / totalLength * 100) > progress) {
                progress = currentLength.toDouble() / totalLength * 100
                launch(Dispatchers.Main) {
                    // TODO progress
                }
            }

            len = ins.read(buff)
        }

        launch(Dispatchers.Main) {
            // TODO success
        }
    } catch (e: Exception) {
        e.printStackTrace()
        launch(Dispatchers.Main) {
            // TODO failed
        }
    } finally {
        ins.close()
        fos.close()
    }
}