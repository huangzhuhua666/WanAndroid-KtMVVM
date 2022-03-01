package com.example.hzh.ktmvvm.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.model.ArticleModel

/**
 * @author huangzhuhua
 * @date 2022/2/28
 */
class SearchPageSource(
    private val model: ArticleModel,
    private val keyword: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            if (keyword.trim().isEmpty()) {
                return LoadResult.Error(Exception())
            }

            val nextPage = params.key ?: 0
            val response = model.search(nextPage, keyword)

            LoadResult.Page(
                data = response.datas,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = if (response.over) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = state.anchorPosition?.let {
        val anchorPage = state.closestPageToPosition(it)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
}