package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.library.viewmodel.BaseVM
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionVM : BaseVM() {

    private val articleModel by lazy { ArticleModel() }

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> = _articleList

    override fun getInitData(isRefresh: Boolean) {
        isLoadMore = false
        pageNo = 0
        doOnIO(
            tryBlock = {
                _isShowLoading.postValue(true)
                articleModel.getCollections(pageNo).run {
                    _articleList.postValue(datas)
                    _isOver.postValue(over)
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
                articleModel.getCollections(pageNo).run {
                    _articleList.postValue(datas)
                    _isOver.postValue(over)
                }
            },
            catchBlock = { e ->
                e.printStackTrace()
                --pageNo
            }
        )
    }

    fun unCollect(article: Article) {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = {
                articleModel.unCollectMyCollection(article.articleId, article.originId).also {
                    _toastTip.postValue(R.string.uncollect_success)
                    LiveEventBus.get("uncollect").post(false)
                    getInitData(false)
                }
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { _isShowLoading.value = false }
        )
    }
}