package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
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

    val treeList = MutableLiveData<List<Category>>()

    val articleList = MutableLiveData<List<Article>>()

    fun getProjectTree() {
        doOnIO(
            tryBlock = { treeList.postValue(categoryModel.getProjectTree()) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun getInitData(isRefresh: Boolean) {
        isLoadMore = false
        isShowLoading.value = !isRefresh
        doOnIO(
            tryBlock = {
                pageNo = when (cid) {
                    -1 -> 1
                    else -> 0
                }
                articleModel.getProjectArticle(pageNo, cid).let {
                    articleList.postValue(it.datas)
                    isOver.postValue(it.over)
                }
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { isShowLoading.value = false }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = {
                articleModel.getProjectArticle(pageNo, cid).let {
                    articleList.postValue(it.datas)
                    isOver.postValue(it.over)
                }
            },
            catchBlock = { e ->
                e.printStackTrace()
                --pageNo
            }
        )
    }
}