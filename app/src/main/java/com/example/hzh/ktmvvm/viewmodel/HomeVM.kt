package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.data.model.BannerBean
import com.example.hzh.ktmvvm.data.network.HomeApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.*

/**
 * Create by hzh on 2019/09/11.
 */
class HomeVM : BaseVM() {

    private var pageNo = 0

    private val service by lazy { App.httpClient.getService(HomeApi::class.java) }

    var isLoadMore = false

    var bannerList = MutableLiveData<List<BannerBean>>()

    var articleList = MutableLiveData<List<ArticleBean>>()

    fun getInitData() {
        isLoadMore = false
        pageNo = 0
        launch(Dispatchers.IO) {
            try {
                service.let {
                    bannerList.postValue(it.getBanners())
                    articleList.postValue(it.getTopArticles().plus(it.getArticles(pageNo).datas))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadArticles() {
        isLoadMore = true
        launch(Dispatchers.IO) {
            try {
                articleList.postValue(service.getArticles(++pageNo).datas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}