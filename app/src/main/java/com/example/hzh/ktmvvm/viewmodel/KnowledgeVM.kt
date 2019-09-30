package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.network.KnowledgeApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/18.
 */
class KnowledgeVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(KnowledgeApi::class.java) }

    var cid by Delegates.notNull<Int>()

    val treeList = MutableLiveData<List<Category>>()

    val articleList = MutableLiveData<List<Article>>()

    fun getSystemTree() {
        doOnIO(
            tryBlock = { treeList.postValue(service.getKnowledgeTree()) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun getInitData() {
        super.getInitData()
        doOnIO(
            tryBlock = { articleList.postValue(service.getKnowledgeArticles(pageNo, cid).datas) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = { articleList.postValue(service.getKnowledgeArticles(pageNo, cid).datas) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }
}