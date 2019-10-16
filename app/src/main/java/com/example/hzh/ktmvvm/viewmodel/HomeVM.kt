package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/09/11.
 */
class HomeVM : BaseVM() {

    private val articleModel by lazy { ArticleModel() }

    val bannerList = MutableLiveData<List<Banner>>()

    val articleList = MutableLiveData<List<Article>>()

    override fun getInitData() {
        super.getInitData()
        isShowLoading.value = true
        doOnIO(
            tryBlock = {
                articleModel.let {
                    bannerList.postValue(it.getBanner())

                    it.getHomeArticle(pageNo).run {
                        articleList.postValue(datas)
                        isOver.postValue(over)
                    }
                }
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { isShowLoading.value = false }
        )
    }

    override fun loadData() {
        super.loadData()
        isShowLoading.value = true
        doOnIO(
            tryBlock = {
                articleModel.getHomeArticle(pageNo).let {
                    articleList.postValue(it.datas)
                    isOver.postValue(it.over)
                }
            },
            catchBlock = { e ->
                e.printStackTrace()
                --pageNo
            },
            finallyBlock = { isShowLoading.value = false }
        )
    }
}