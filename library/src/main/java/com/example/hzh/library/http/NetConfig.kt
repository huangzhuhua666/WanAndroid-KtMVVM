package com.example.hzh.library.http

import com.example.hzh.library.extension.DelegateExt
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/08/26.
 */
object NetConfig {

    var CODE_SUCCESS by Delegates.notNull<Int>()

    var CODE_NO_RESPONSE_BODY = 1015

    var CODE_LOGIN_EXPIRED by Delegates.notNull<Int>()

    var CONNECT_TIMEOUT = 10L

    var READ_TIMEOUT = 10L

    var WRITE_TIMEOUT = 10L

    var DOMAIN = ""

    var BASE_URL by DelegateExt.notNullSingleValue<String>()

}