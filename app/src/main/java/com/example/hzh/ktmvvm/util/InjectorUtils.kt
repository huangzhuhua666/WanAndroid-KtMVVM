package com.example.hzh.ktmvvm.util

import com.example.hzh.ktmvvm.viewmodel.WebVMFactory

/**
 * Create by hzh on 2021/11/26
 */
object InjectorUtils {

    fun provideWebVMFactory(
        originTitle: String
    ): WebVMFactory = WebVMFactory(originTitle = originTitle)
}