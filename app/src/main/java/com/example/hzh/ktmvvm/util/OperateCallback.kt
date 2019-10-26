package com.example.hzh.ktmvvm.util

import androidx.annotation.StringRes

/**
 * Create by hzh on 2019/10/22.
 */
interface OperateCallback<T> {

    fun onInputIllegal(@StringRes tip: Int)

    fun onPreOperate()

    fun onCallback(data: T)
}