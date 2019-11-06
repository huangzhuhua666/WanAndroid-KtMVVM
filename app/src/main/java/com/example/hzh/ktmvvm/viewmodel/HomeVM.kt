package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/09/11.
 */
class HomeVM : BaseVM() {

    private val articleModel by lazy { ArticleModel() }

    private val _bannerList = MutableLiveData<List<Banner>>()
    val bannerList: LiveData<List<Banner>> = _bannerList

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> = _articleList

    override fun getInitData(isRefresh: Boolean) {
        super.getInitData(isRefresh)
        doOnIO(
            tryBlock = {
                articleModel.let {
                    _bannerList.postValue(it.getBanner())

                    it.getHomeArticle(pageNo).run {
                        _articleList.postValue(datas)
                        _isOver.postValue(over)
                    }
                }
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = {
                articleModel.getHomeArticle(pageNo).let {
                    _articleList.postValue(it.datas)
                    _isOver.postValue(it.over)
                }
            },
            catchBlock = { e ->
                e.printStackTrace()
                --pageNo
            }
        )
    }

    /**
     * 收藏、取消收藏
     * @param article 文章
     */
    fun collectOrNot(article: Article) {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = {
                if (article.collect) articleModel.unCollectArticleList(article.articleId).also {
                    _toastTip.postValue(R.string.uncollect_success)
                    article.collect = false
                } else articleModel.collectionInner(article.articleId).also {
                    _toastTip.postValue(R.string.collect_success)
                    article.collect = true
                }
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { _isShowLoading.value = false }
        )
    }
}