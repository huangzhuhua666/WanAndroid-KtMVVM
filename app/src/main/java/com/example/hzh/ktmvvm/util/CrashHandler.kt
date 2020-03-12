package com.example.hzh.ktmvvm.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import com.example.hzh.library.extension.createFile
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * Create by hzh on 2020/3/11.
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    private val mDefaultHandler by lazy { Thread.getDefaultUncaughtExceptionHandler() }

    private lateinit var mContext: Context

    private var hasInit: Boolean = false
    private val mDeviceInfo by lazy { hashMapOf<String, String>() }
    private val sdf by lazy { SimpleDateFormat("MM-dd", Locale.CHINA) }

    companion object {

        fun getInstance(): CrashHandler = HOLDER.instance
    }

    fun init(context: Context) {
        if (hasInit) throw RuntimeException("CrashHandler has been initialized!")

        hasInit = true
        mContext = context
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleException(t, e) && mDefaultHandler != null)
            mDefaultHandler!!.uncaughtException(t, e)
        else {
            try {
                Thread.sleep(3000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
    }

    private fun handleException(t: Thread, e: Throwable?): Boolean = if (e == null) false
    else {
        mDeviceInfo["thread_id"] = t.id.toString()
        mDeviceInfo["thread_name"] = t.name

        collectDeviceInfo() // 收集系统信息
        saveCrashInfoToFile(e) // 保存崩溃信息
        true
    }

    private fun collectDeviceInfo() {
        try {
            mContext.packageManager.getPackageInfo(
                mContext.packageName,
                PackageManager.GET_ACTIVITIES
            )?.run {
                mDeviceInfo["version_code"] = versionCode.toString()
                mDeviceInfo["version_name"] = versionName ?: ""
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        Build::class.java.declaredFields.forEach {
            try {
                it.isAccessible = true
                mDeviceInfo[it.name] = it?.get(null).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun saveCrashInfoToFile(e: Throwable) {
        val sb = StringBuffer().apply {
            mDeviceInfo.forEach { append("${it.key.toLowerCase(Locale.CHINA)} = ${it.value}\n") }
        }

        val writer = StringWriter()

        PrintWriter(writer).let {
            e.printStackTrace(it)

            var cause = e.cause
            while (cause != null) {
                cause.printStackTrace(it)
                cause = cause.cause
            }

            it.close()
        }

        sb.append(writer.toString())

        if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) return

        try {
            val currTime = System.currentTimeMillis()
            val crashFile = File(mContext.createFile("crash"), sdf.format(currTime)).let {
                if (!it.exists()) it.mkdirs()

                File(it, "crash-${currTime}.log")
            }

            FileOutputStream(crashFile).apply {
                write(sb.toString().toByteArray())
                close()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    object HOLDER {
        val instance = CrashHandler()
    }
}