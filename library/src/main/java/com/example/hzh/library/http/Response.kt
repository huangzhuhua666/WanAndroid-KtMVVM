package com.example.hzh.library.http

import com.alibaba.fastjson.annotation.JSONField

/**
 * Create by hzh on 2019/08/26.
 */
class Response<T> {

    @JSONField(name = "errorCode")
    var code: Int = 0

    @JSONField(name = "errorMsg")
    var msg: String = ""

    @JSONField(name = "data")
    var obj: T? = null

    fun isSuccess(): Boolean = code == NetConfig.CODE_SUCCESS

    fun isNoResponseBody(): Boolean = isSuccess() && obj == null
}