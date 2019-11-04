package com.example.hzh.ktmvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.network.CollectApi
import com.example.hzh.library.http.APIException
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(CollectApi::class.java) }

    val articleList = MutableLiveData<List<Article>>()

    override fun getInitData(isFirstIn: Boolean) {
        super.getInitData(isFirstIn)
        doOnIO(
            tryBlock = { articleList.postValue(service.getCollections(pageNo).datas) },
            catchBlock = { e ->
                when (e) {
                    is APIException -> if (e.isLoginExpired()) Log.d("TAG", "Login Please")
                }
            },
            finallyBlock = { isShowLoading.value = false }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = { articleList.postValue(service.getCollections(pageNo).datas) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }
}