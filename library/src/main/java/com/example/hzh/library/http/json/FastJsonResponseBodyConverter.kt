package com.example.hzh.library.http.json

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig
import com.example.hzh.library.http.APIException
import com.example.hzh.library.http.NetConfig
import com.example.hzh.library.http.Response
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.Exception
import java.lang.reflect.Type

/**
 * Create by hzh on 2019/08/24.
 */
class FastJsonResponseBodyConverter<T>(
    private val type: Type,
    private val config: ParserConfig,
    private val featureValues: Int,
    private val features: Array<Feature>?
) : Converter<ResponseBody, T> {

    companion object {
        private val EMPTY_SERIALIZER_FEATURE = arrayOfNulls<Feature>(0)
    }

    override fun convert(value: ResponseBody): T? {
        var json = value.string()
        val temp = JSON.parseObject(json, Response::class.java)

        if (!temp.isSuccess()) throw APIException(temp.code, temp.msg)

        if (temp.isNoResponseBody()) throw APIException(
            NetConfig.CODE_NO_RESPONSE_BODY,
            temp.msg
        )

        return try {
            json = temp.obj.toString()
            JSON.parseObject(
                json,
                type,
                config,
                featureValues,
                *features ?: EMPTY_SERIALIZER_FEATURE
            )
        } catch (e: Exception) {
            json = "\"$json\""
            JSON.parseObject(
                json,
                type,
                config,
                featureValues,
                *features ?: EMPTY_SERIALIZER_FEATURE
            )
        } finally {
            value.close()
        }
    }
}