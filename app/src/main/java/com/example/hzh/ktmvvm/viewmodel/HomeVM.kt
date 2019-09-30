package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.network.HomeApi
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/09/11.
 */
class HomeVM : BaseVM() {

    private val service by lazy { App.httpClient.getService(HomeApi::class.java) }

    val bannerList = MutableLiveData<List<Banner>>()

    val articleList = MutableLiveData<List<Article>>()

    override fun getInitData() {
        super.getInitData()
        doOnIO(
            tryBlock = {
                service.let {
                    bannerList.postValue(it.getBanners())
                    articleList.postValue(it.getTopArticles().plus(it.getArticles(pageNo).datas))
                }
            },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = { articleList.postValue(service.getArticles(pageNo).datas) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }
}