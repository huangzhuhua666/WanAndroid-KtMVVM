package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.ktmvvm.data.model.CacheModel
import com.example.hzh.ktmvvm.data.model.WebsiteModel
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/11/11.
 */
class SearchVM : BaseVM() {

    private val cacheModel by lazy { CacheModel() }
    private val websiteModel by lazy { WebsiteModel() }
    private val articleModel by lazy { ArticleModel() }

    private val _historyList = MutableLiveData<List<String>>(listOf())
    val historyList: LiveData<List<String>> = _historyList

    private val _hotKeyList = MutableLiveData<List<Website>>()
    val hotKeyList: LiveData<List<Website>> = _hotKeyList

    private val _commonWebList = MutableLiveData<List<Website>>()
    val commonWebList: LiveData<List<Website>> = _commonWebList

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> = _articleList

    val isResult = MutableLiveData<Boolean>(false)

    private var k = ""
    var keyword = MutableLiveData<String>()

    override fun getInitData(isRefresh: Boolean) {
        k = keyword.value ?: ""
        if (k == "") return
        super.getInitData(isRefresh)
        isResult.value = true
        // 保存搜索历史
        saveHistory(k)
        doOnIO(
            tryBlock = {
                articleModel.search(pageNo, k).let {
                    _articleList.postValue(it.datas)
                    _isOver.postValue(it.over)
                }
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = {
                articleModel.search(pageNo, k).let {
                    _articleList.postValue(_articleList.value?.plus(it.datas))
                    _isOver.postValue(it.over)
                }
            },
            catchBlock = { --pageNo },
            finallyBlock = { isFinish.value = true }
        )
    }

    /**
     * 获取搜索历史
     */
    fun getHistory() = cacheModel.getHistory()?.let { _historyList.postValue(it) }

    /**
     * 保存搜索历史
     * @param data
     */
    fun saveHistory(data: String) = cacheModel.run {
        saveHistory(data).also { _historyList.postValue(getHistory() ?: listOf()) }
    }

    /**
     * 清空搜索历史
     */
    fun cleanHistory() = cacheModel.cleanHistory().also { _historyList.postValue(listOf()) }

    /**
     * 获取热搜词
     */
    fun getHotKey() {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = { _hotKeyList.postValue(websiteModel.getHotKey()) },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    /**
     * 获取常用网站
     */
    fun getCommonWebsite() {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = { _commonWebList.postValue(websiteModel.getCommonWebsite()) },
            finallyBlock = { _isShowLoading.value = false }
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
            finallyBlock = { _isShowLoading.value = false }
        )
    }
}