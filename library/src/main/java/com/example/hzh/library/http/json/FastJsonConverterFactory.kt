package com.example.hzh.library.http.json

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Create by hzh on 2019/08/24.
 */
class FastJsonConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create(): FastJsonConverterFactory =
            FastJsonConverterFactory()
    }

    var parserConfig: ParserConfig = ParserConfig.global

    private val featureValues = JSON.DEFAULT_PARSER_FEATURE

    var features: Array<Feature>? = null

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return super.responseBodyConverter(type, annotations, retrofit)
    }
}