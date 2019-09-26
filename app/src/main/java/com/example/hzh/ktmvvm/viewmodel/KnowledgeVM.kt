package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.ktmvvm.data.network.KnowledgeApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/18.
 */
class KnowledgeVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(KnowledgeApi::class.java) }

    var cid by Delegates.notNull<Int>()

    val treeList = MutableLiveData<List<CategoryBean>>()

    val articleList = MutableLiveData<List<ArticleBean>>()

    fun getSystemTree() {
        launch(Dispatchers.IO) {
            try {
                treeList.postValue(service.getKnowledgeTree())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getInitData() {
        super.getInitData()
        launch(Dispatchers.IO) {
            try {
                articleList.postValue(service.getKnowledgeArticles(pageNo, cid).datas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadData() {
        super.loadData()
        launch(Dispatchers.IO) {
            try {
                articleList.postValue(service.getKnowledgeArticles(pageNo, cid).datas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}