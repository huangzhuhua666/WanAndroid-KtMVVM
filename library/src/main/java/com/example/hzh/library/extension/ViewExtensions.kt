package com.example.hzh.library.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

/**
 * Create by hzh on 2019/09/12.
 */
fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(resId), duration)
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun <T : ViewModel> FragmentActivity.obtainVM(modelClass: Class<T>): T =
    ViewModelProviders.of(this)[modelClass]

fun <T : ViewModel> Fragment.obtainVM(modelClass: Class<T>): T =
    ViewModelProviders.of(this)[modelClass]