package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
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

    var authorList = MutableLiveData<List<Category>>()

    var articleList = MutableLiveData<List<Article>>()

    fun getWeChatAuthors() {
        doOnIO(
            tryBlock = { authorList.postValue(categoryModel.getWeChatAuthors()) },
            catchBlock = { e -> e.printStackTrace() }
        )
    }

    override fun getInitData() {
        isLoadMore = false
        pageNo = 1
        isShowLoading.value = true
        doOnIO(
            tryBlock = {
                articleModel.getWeChatArticle(id.value!!, pageNo).let {
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
        isShowLoading.value = true
        doOnIO(
            tryBlock = {
                articleModel.getWeChatArticle(id.value!!, pageNo).let {
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