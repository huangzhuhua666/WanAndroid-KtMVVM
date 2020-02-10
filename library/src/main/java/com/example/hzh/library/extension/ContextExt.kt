package com.example.hzh.library.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
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

/**
 * 创建一个File
 */
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

/**
 * 隐藏软键盘
 */
fun Activity.hideKeyboard(view: View) {
    view.run {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

inline fun <reified A : Activity> Activity.startActivity(bundle: Bundle? = null) =
    if (bundle == null) startActivity(Intent(this, A::class.java))
    else startActivity(Intent(this, A::class.java).apply { putExtras(bundle) })

inline fun <reified A : Activity> Activity.startActivityForResult(
    bundle: Bundle? = null,
    requestCode: Int
) = if (bundle == null) startActivityForResult(Intent(this, A::class.java), requestCode)
else startActivityForResult(Intent(this, A::class.java).apply { putExtras(bundle) }, requestCode)

inline fun <reified F : Fragment> newFragment(bundle: Bundle? = null) =
    if (bundle == null) F::class.java.newInstance()
    else F::class.java.newInstance().apply { arguments = bundle }