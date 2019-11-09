package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.model.ArticleModel
import com.example.hzh.ktmvvm.data.model.CategoryModel
import com.example.hzh.library.viewmodel.BaseVM
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/21.
 */
class ProjectVM : BaseVM() {

    private val categoryModel by lazy { CategoryModel() }
    private val articleModel by lazy { ArticleModel() }

    var cid by Delegates.notNull<Int>()

    private val _treeList = MutableLiveData<List<Category>>()
    val treeList: LiveData<List<Category>> = _treeList

    private val _articleList = MutableLiveData<List<Article>>()
    val articleList: LiveData<List<Article>> = _articleList

    /**
     * 获取项目分类
     */
    fun getProjectTree() {
        doOnIO(
            tryBlock = { _treeList.postValue(categoryModel.getProjectTree()) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun getInitData(isRefresh: Boolean) {
        isLoadMore = false
        _isShowLoading.value = !isRefresh
        doOnIO(
            tryBlock = {
                pageNo = when (cid) {
                    -1 -> 1 // 最新项目页码从1开始
                    else -> 0 // 其他项目分类页码从0开始
                }
                articleModel.getProjectArticle(pageNo, cid).let {
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
                articleModel.getProjectArticle(pageNo, cid).let {
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