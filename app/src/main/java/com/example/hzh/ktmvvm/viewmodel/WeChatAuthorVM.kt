package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.ktmvvm.data.network.WeChatAuthorApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Create by hzh on 2019/9/21.
 */
class WeChatAuthorVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(WeChatAuthorApi::class.java) }

    var id = MutableLiveData(-1)

    var keyword = MutableLiveData("")

    var authorList = MutableLiveData<List<CategoryBean>>()

    var articleList = MutableLiveData<List<ArticleBean>>()

    fun getWeChatAuthors() {
        launch(Dispatchers.IO) {
            try {
                authorList.postValue(service.getWeChatAuthors())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getInitData() {
        isLoadMore = false
        pageNo = 1
        launch(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadData() {
        super.loadData()
        launch(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}