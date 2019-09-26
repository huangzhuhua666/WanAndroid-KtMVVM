package com.example.hzh.ktmvvm.viewmodel

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.network.CollectApi
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(CollectApi::class.java) }


}