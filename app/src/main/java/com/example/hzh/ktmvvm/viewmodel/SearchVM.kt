package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.ktmvvm.data.model.CacheModel
import com.example.hzh.ktmvvm.data.model.WebsiteModel
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Create by hzh on 2021/12/8
 */
class SearchVM(
    private val cacheModel: CacheModel,
    private val websiteModel: WebsiteModel,
    private val articleModel: ArticleModel,
) : BaseVM() {

    /**
     * 搜索历史
     */
    private val _historyList = MutableStateFlow<List<String>>(listOf())
    val historyList: StateFlow<List<String>>
        get() = _historyList

    /**
     * 热搜词
     */
    private val _hotKeyList = MutableStateFlow<List<Website>>(listOf())
    val hotKeyList: StateFlow<List<Website>>
        get() = _hotKeyList

    /**
     * 常用网站
     */
    private val _commonWebList = MutableStateFlow<List<Website>>(listOf())
    val commonWebList: StateFlow<List<Website>>
        get() = _commonWebList

    /**
     * 搜索结果
     */
    private val _articleList = MutableStateFlow<List<Article>>(listOf())
    val articleList: StateFlow<List<Article>>
        get() = _articleList

    private val _isResultPage = MutableStateFlow(false)
    val isResultPage: StateFlow<Boolean>
        get() = _isResultPage

    private var keyword = ""

    init {
        getHistory()
        getHotKey()
        getCommonWebsite()
    }

    /**
     * 搜索
     */
    fun search(keyword: String) {
        if (keyword.trim().isEmpty()) return
        this.keyword = keyword
        updatePage(true)

        super.getInitData(false)

        // 保存搜索历史
        saveHistory(keyword)

        doOnIO(
            tryBlock = {
                articleModel.search(pageNo, keyword).let {
                    _articleList.value = it.datas
                    _isOver.postValue(it.over)
                }
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    override fun loadData() {
        if (keyword.trim().isEmpty()) return

        super.loadData()

        doOnIO(
            tryBlock = {
                articleModel.search(pageNo, keyword).let {
                    _articleList.value = _articleList.value.plus(it.datas)
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
    private fun getHistory() = cacheModel.getHistory()?.let { _historyList.value = it }

    /**
     * 获取热搜词
     */
    private fun getHotKey() = doOnIO(tryBlock = { _hotKeyList.value = websiteModel.getHotKey() })

    /**
     * 获取常用网站
     */
    private fun getCommonWebsite() =
        doOnIO(tryBlock = { _commonWebList.value = websiteModel.getCommonWebsite() })

    /**
     * 保存搜索历史
     * @param data keyword
     */
    private fun saveHistory(data: String) = cacheModel.run {
        saveHistory(data).also { _historyList.value = getHistory() ?: listOf() }
    }

    /**
     * 清空搜索历史
     */
    fun cleanHistory() = cacheModel.cleanHistory().also { _historyList.value = listOf() }

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

    fun updatePage(isResultPage: Boolean) {
        _isResultPage.value = isResultPage
    }
}

class SearchVMFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SearchVM(
        cacheModel = CacheModel(),
        websiteModel = WebsiteModel(),
        articleModel = ArticleModel()
    ) as T
}