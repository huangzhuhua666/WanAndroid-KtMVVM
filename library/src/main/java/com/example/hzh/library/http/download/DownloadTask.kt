package com.example.hzh.library.http.download

import android.os.AsyncTask
import android.util.Log
import com.example.hzh.library.extension.closeSave
import com.example.hzh.library.extension.roundAndScale
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Create by hzh on 2020/1/16.
 */
class DownloadTask(
    val file: File,
    private var onStart: () -> Unit = {},
    private var onProgress: (Double) -> Unit = {},
    private var onCanceled: () -> Unit = {},
    private var onSuccess: () -> Unit = {},
    private var onFailed: () -> Unit = {}
) : AsyncTask<String, Double, DownloadTask.Status>() {

    companion object {
        private const val TAG = "download"
    }

    private var isCancelDownload = false

    override fun onPreExecute() {
        Log.d(TAG, "download start")
        onStart.invoke()
    }

    override fun doInBackground(vararg params: String?): Status {
        if (params[0] == null) return Status.FAILED

        if (file.exists()) file.delete()

        var ins: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            val response = OkHttpClient().newCall(Request.Builder().run {
                url(params[0]!!)
                build()
            }).execute()

            if (!response.isSuccessful || response.body() == null) return Status.FAILED

            response.body()!!.let { body ->
                ins = body.byteStream()
                fos = FileOutputStream(file)

                val totalLength = body.contentLength()
                var currentLength = 0L
                var progress = 0.0

                // 读写文件
                var len: Int
                val buffer = ByteArray(1024)
                while (ins!!.read(buffer).also { len = it } != -1) {
                    if (isCancelDownload) return Status.CANCELED

                    fos!!.write(buffer, 0, len)

                    currentLength += len

                    // 避免频繁更新
                    if ((currentLength.toDouble() / totalLength * 100).roundAndScale(1) > progress) {
                        progress = (currentLength.toDouble() / totalLength * 100).roundAndScale(1)
                        publishProgress(progress)
                    }
                }

                body.close()

                return Status.SUCCESS
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Status.FAILED
        } finally {
            ins?.closeSave()
            fos?.closeSave()
        }
    }

    override fun onProgressUpdate(vararg values: Double?) {
        values[0]?.let {
            Log.d(TAG, "update progress:$it%")
            onProgress.invoke(it)
        }
    }

    override fun onPostExecute(result: Status?) {
        result?.let {
            when (it) {
                Status.CANCELED -> {
                    Log.d(TAG, "download cancel")
                    onCanceled.invoke()
                }
                Status.SUCCESS -> {
                    Log.d(TAG, "download success")
                    onSuccess.invoke()
                }
                Status.FAILED -> {
                    Log.d(TAG, "download failed")
                    onFailed.invoke()
                }
            }
        }
    }

    fun cancelDownload() {
        isCancelDownload = true
    }

    enum class Status {
        SUCCESS, CANCELED, FAILED
    }
}