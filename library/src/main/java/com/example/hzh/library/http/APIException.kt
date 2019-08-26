package com.example.hzh.library.http

/**
 * Create by hzh on 2019/08/26.
 */
class APIException(
    var code: Int,
    var msg: String
) : Exception() {
    fun isLoginExpired(): Boolean = code == NetConfig.CODE_LOGIN_EXPIRED
}