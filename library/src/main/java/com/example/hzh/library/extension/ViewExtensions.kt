package com.example.hzh.library.extension

import android.content.Context
import android.widget.Toast

/**
 * Create by hzh on 2019/09/12.
 */
fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}