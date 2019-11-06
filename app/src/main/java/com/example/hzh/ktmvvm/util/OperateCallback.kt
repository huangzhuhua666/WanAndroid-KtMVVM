package com.example.hzh.ktmvvm.util

import androidx.annotation.StringRes

/**
 * Create by hzh on 2019/10/22.
 */
interface OperateCallback<T> {

    /**
     * 非法输入回调
     * @param tip 提示信息
     */
    fun onInputIllegal(@StringRes tip: Int)

    /**
     * 网络请求前的操作回调
     */
    fun onPreOperate()

    /**
     * 操作成功回调
     */
    fun onCallback(data: T)
}