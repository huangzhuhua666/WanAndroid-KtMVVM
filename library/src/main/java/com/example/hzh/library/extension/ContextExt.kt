package com.example.hzh.library.extension

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import java.io.File
import java.io.IOException

/**
 * Create by hzh on 2019/09/12.
 */
fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(resId), duration)
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.inflate(@LayoutRes resource: Int, root: ViewGroup? = null): View =
    LayoutInflater.from(this).inflate(resource, root)

fun Context.createFile(name: String): File {
    if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) throw IOException("create file failed")

    val type = when (name.substringAfterLast('.')) {
        "apk" -> Environment.DIRECTORY_DOWNLOADS
        "jpg", "jpeg", "png", "gif" -> Environment.DIRECTORY_PICTURES
        "mp3" -> Environment.DIRECTORY_MUSIC
        "mp4", "rmvb", "mkv" -> Environment.DIRECTORY_MOVIES
        else -> Environment.DIRECTORY_DOCUMENTS
    }

    getExternalFilesDir(type)?.let {
        if (!it.exists()) it.mkdirs()
        return File(it, name)
    }
    throw IOException("create file failed")
}

fun Activity.hideKeyboard(view: View) {
    view.run {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}