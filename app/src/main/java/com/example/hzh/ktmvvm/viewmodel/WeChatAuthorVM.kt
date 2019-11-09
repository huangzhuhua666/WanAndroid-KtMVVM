package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.ktmvvm.data.model.CategoryModel
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/9/21.
 */
class WeChatAuthorVM : BaseVM() {

    private val categoryModel by lazy { CategoryModel() }
    private val articleModel by lazy { ArticleModel() }

    var id = MutableLiveData(-1)

    var keyword = MutableLiveData("")

    private val _authorList = MutableLiveData<List<Category>>()
    val authorList: LiveData<List<Category>> = _authorList

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> = _articleList

    /**
     * 获取微信公众号
     */
    fun getWeChatAuthors() {
        doOnIO(
            tryBlock = { _authorList.postValue(categoryModel.getWeChatAuthors()) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun getInitData(isRefresh: Boolean) {
        isLoadMore = false
        pageNo = 1
        _isShowLoading.value = !isRefresh
        doOnIO(
            tryBlock = {
                articleModel.getWeChatArticle(id.value!!, pageNo).let {
                    _articleList.postValue(it.datas)
                    _isOver.postValue(it.over)
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
                articleModel.getWeChatArticle(id.value!!, pageNo).let {
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
                    // 取消收藏
                    _toastTip.postValue(R.string.uncollect_success)
                    article.collect = false
                } else articleModel.collectInner(article.articleId).also {
                    // 收藏
                    _toastTip.postValue(R.string.collect_success)
                    article.collect = true
                }
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { _isShowLoading.value = false }
        )
    }
}