package com.example.hzh.library.http.download

import android.util.Log
import com.example.hzh.library.extension.closeSave
import com.example.hzh.library.extension.roundAndScale
import com.example.hzh.library.extension.write
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream

/**
 * Create by hzh on 2019/12/21.
 */
private const val TAG = "download"

suspend fun ResponseBody.saveFile(
    file: File,
    listener: DownloadListener? = null
) = coroutineScope {
    val ins = byteStream()
    val fos = FileOutputStream(file)
    try {
        withContext(Dispatchers.IO) {
            val totalLength = contentLength()
            var currentLength = 0L
            var progress = 0.0

            // 读写文件
            fos.write(ins) {
                currentLength += it

                // 避免频繁更新
                if ((currentLength.toDouble() / totalLength * 100).roundAndScale(1) > progress) {
                    progress = (currentLength.toDouble() / totalLength * 100).roundAndScale(1)
                    // 主线程更新进度
                    launch(Dispatchers.Main) {
                        Log.d(TAG, "update progress:$progress%")
                        listener?.onProgress(progress)
                    }
                }
            }

            // 下载成功
            launch(Dispatchers.Main) {
                Log.d(TAG, "download success")
                listener?.onSuccess(file)
            }
        }
    } catch (e: CancellationException) { // 取消下载
        e.printStackTrace()
        Log.d(TAG, "download cancel")
        listener?.onCancel()
    } catch (e: Exception) { // 下载失败
        e.printStackTrace()
        Log.d(TAG, "download failed")
        listener?.onFailed()
    } finally {
        ins.closeSave()
        fos.closeSave()
    }
}
