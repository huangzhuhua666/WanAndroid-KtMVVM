package com.example.hzh.ktmvvm.util

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.example.hzh.ktmvvm.data.bean.Article

/**
 * Create by hzh on 2019/11/12.
 */
class ArticleDiffCallback(newList: List<Article>) : BaseQuickDiffCallback<Article>(newList) {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.articleId.compareTo(newItem.articleId) == 0

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
        oldItem.collect == newItem.collect && oldItem.niceDate == newItem.niceDate
}