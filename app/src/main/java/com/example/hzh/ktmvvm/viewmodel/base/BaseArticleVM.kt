package com.example.hzh.ktmvvm.viewmodel.base

import androidx.paging.PagingData
import androidx.paging.map
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.event.ArticleModifyEvent
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * @author huangzhuhua
 * @date 2022/3/7
 */
open class BaseArticleVM : BaseVM() {

    private val mModificationEvents = MutableStateFlow<List<ArticleModifyEvent>>(emptyList())

    protected fun refreshCollect(article: Article) {
        addModify(ArticleModifyEvent.Edit(article.articleId, !article.collect))
    }

    protected fun addModify(event: ArticleModifyEvent) {
        mModificationEvents.value += event
    }

    protected fun Flow<PagingData<Article>>.combineModify() =
        combine(mModificationEvents) { pagingData, modifications ->
            modifications.fold(pagingData) { acc, event ->
                handleEvent(acc, event)
            }
        }

    private fun handleEvent(
        pagingData: PagingData<Article>,
        event: ArticleModifyEvent
    ): PagingData<Article> = when (event) {
        is ArticleModifyEvent.Edit -> {
            pagingData.map {
                if (event.id == it.articleId) it.copy(event.collect) else it
            }
        }
    }
}