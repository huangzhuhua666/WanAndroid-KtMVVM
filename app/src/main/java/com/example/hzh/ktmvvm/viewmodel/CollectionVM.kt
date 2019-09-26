package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.ArticleBean
import com.example.hzh.ktmvvm.data.network.CollectApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(CollectApi::class.java) }

    val articleList = MutableLiveData<List<ArticleBean>>()

    override fun getInitData() {
        super.getInitData()
        launch(Dispatchers.IO) {
            try {
                articleList.postValue(service.getCollections(pageNo).datas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadData() {
        super.loadData()
        launch(Dispatchers.IO) {
            try {
                articleList.postValue(service.getCollections(pageNo).datas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}