package com.example.hzh.ktmvvm.work

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hzh.library.extension.createFile

/**
 * Create by hzh on 2020/3/12.
 */
class UploadCrashDemoCrash(
    private val context: Context, params: WorkerParameters
) : Worker(context, params) {

    companion object {

        private const val TAG = "UploadCrashDemoCrash"
    }

    override fun doWork(): Result = try {
        context.createFile("crash").let {
            if (!it.exists()) return@let Result.success()
            else it.listFiles()?.forEach { files ->
                if (files.isDirectory) files.listFiles()?.forEach { file -> file.delete() }

                files.delete()
            }
        }

        Log.d(TAG, "Work Done")
        Result.success()
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure()
    }
}