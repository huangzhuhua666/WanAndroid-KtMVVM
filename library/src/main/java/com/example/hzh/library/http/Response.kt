package com.example.hzh.library.http

/**
 * Create by hzh on 2019/08/26.
 */
data class Response<T>(
    var code: Int,
    var msg: String,
    var obj: T?
) {
    fun isSuccess(): Boolean = code == NetConfig.CODE_SUCCESS

    fun isNoResponseBody(): Boolean = isSuccess() && obj == null
}