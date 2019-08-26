package com.example.hzh.library.http.json

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.serializer.SerializerFeature
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

/**
 * Create by hzh on 2019/08/24.
 */
class FastJsonRequestBodyConverter<T>(
    private val config: SerializeConfig?,
    private val features: Array<SerializerFeature>?
) : Converter<T, RequestBody> {

    companion object {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
    }

    override fun convert(value: T): RequestBody? {
        val content = if (config != null) {
            if (features != null) JSON.toJSONBytes(value, config, *features)
            else JSON.toJSONBytes(value, config)
        } else {
            if (features != null) JSON.toJSONBytes(value, *features)
            else JSON.toJSONBytes(value)
        }
        return RequestBody.create(MEDIA_TYPE, content)
    }
}