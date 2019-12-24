package com.example.hzh.library.http.download

import android.util.Log
import com.example.hzh.library.extension.closeSave
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
    onStart: suspend CoroutineScope.() -> Unit = {},
    onProgress: suspend CoroutineScope.(Double) -> Unit = {},
    onSuccess: suspend CoroutineScope.() -> Unit = {},
    onFailed: suspend CoroutineScope.() -> Unit = {},
    onCancel: suspend CoroutineScope.() -> Unit = {}
) = coroutineScope {
    Log.d(TAG, "download start")
    onStart()

    val ins = byteStream()
    val fos = FileOutputStream(file)
    try {
        withContext(Dispatchers.IO) {
            val totalLength = contentLength()
            var currentLength = 0L
            var progress = 0.0

            fos.write(ins) {
                currentLength += it

                if ((currentLength.toDouble() / totalLength * 100) > progress) {
                    progress = currentLength.toDouble() / totalLength * 100
                    launch(Dispatchers.Main) {
                        Log.d(TAG, "update progress")
                        onProgress(progress)
                    }
                }
            }

            launch(Dispatchers.Main) {
                Log.d(TAG, "download success")
                onSuccess()
            }
        }
    } catch (e: CancellationException) {
        e.printStackTrace()
        Log.d(TAG, "download cancel")
        onCancel()
    } catch (e: Exception) {
        e.printStackTrace()
        Log.d(TAG, "download failed")
        onFailed()
    } finally {
        ins.closeSave()
        fos.closeSave()
    }
}
