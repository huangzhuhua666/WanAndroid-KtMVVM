package com.example.hzh.ktmvvm.data.model

import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.bean.Page
import com.example.hzh.ktmvvm.data.repository.ArticleRepository

/**
 * Create by hzh on 2019/9/29.
 */
class ArticleModel {

    private val repo by lazy { ArticleRepository.getInstance() }

    suspend fun getBanner(): List<Banner> = repo.getBanner()

    suspend fun getHomeArticle(pageNo: Int): Page<Article> = repo.run {
        when (pageNo) {
            0 -> getArticles(pageNo).apply { datas = getTopArticles().plus(datas) }
            else -> getArticles(pageNo)
        }
    }

    suspend fun getKnowledgeArticles(pageNo: Int, cid: Int): Page<Article> =
        repo.getKnowledgeArticles(pageNo, cid)
}