package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.network.WeChatAuthorApi
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/9/21.
 */
class WeChatAuthorVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(WeChatAuthorApi::class.java) }

    var id = MutableLiveData(-1)

    var keyword = MutableLiveData("")

    var authorList = MutableLiveData<List<Category>>()

    var articleList = MutableLiveData<List<Article>>()

    fun getWeChatAuthors() {
        doOnIO(
            tryBlock = { authorList.postValue(service.getWeChatAuthors()) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun getInitData() {
        isLoadMore = false
        pageNo = 1
        doOnIO(
            tryBlock = {
                articleList.postValue(
                    when (keyword.value) {
                        "" -> service.getWeChatArticle(id.value!!, pageNo).datas
                        else -> service.searchWeChatArticle(
                            id.value!!,
                            pageNo,
                            keyword.value!!
                        ).datas
                    }
                )
            },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = {
                articleList.postValue(
                    when (keyword.value) {
                        "" -> service.getWeChatArticle(id.value!!, pageNo).datas
                        else -> service.searchWeChatArticle(
                            id.value!!,
                            pageNo,
                            keyword.value!!
                        ).datas
                    }
                )
            },
            catchBlock = { e -> e.printStackTrace() }
        )
    }
}