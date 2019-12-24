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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import java.io.File

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

fun Context.createFile(name: String, type: String = Environment.DIRECTORY_DOCUMENTS): File? {
    if (Environment.getExternalStorageState() == Environment.MEDIA_UNMOUNTED) return null
    getExternalFilesDir(type)?.let {
        if (!it.exists()) it.mkdirs()
        return File(it, name)
    }
    return null
}

fun Activity.hideKeyboard(view: View) {
    view.run {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}