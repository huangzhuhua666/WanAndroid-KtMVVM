package com.example.hzh.library.http.json

import okhttp3.RequestBody
import retrofit2.Converter

/**
 * Create by hzh on 2019/08/24.
 */
class FastJsonResponseBodyConverter<T> : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}