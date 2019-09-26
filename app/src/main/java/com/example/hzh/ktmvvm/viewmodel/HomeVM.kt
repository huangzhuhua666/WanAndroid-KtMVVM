package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.ArticleBean
import com.example.hzh.ktmvvm.data.bean.BannerBean
import com.example.hzh.ktmvvm.data.network.HomeApi
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.*

/**
 * Create by hzh on 2019/09/11.
 */
class HomeVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(HomeApi::class.java) }

    val bannerList = MutableLiveData<List<BannerBean>>()

    val articleList = MutableLiveData<List<ArticleBean>>()

    override fun getInitData() {
        super.getInitData()
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

    override fun loadData() {
        super.loadData()
        launch(Dispatchers.IO) {
            try {
                articleList.postValue(service.getArticles(pageNo).datas)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}