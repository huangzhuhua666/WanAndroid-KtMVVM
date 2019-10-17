package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.bean.Page
import com.example.hzh.ktmvvm.data.network.ArticleApi
import org.litepal.LitePal

/**
 * Create by hzh on 2019/9/29.
 */
class ArticleRepository private constructor() {

    companion object {

        fun getInstance(): ArticleRepository = Holder.mInstance
    }

    private object Holder {
        val mInstance = ArticleRepository()
    }

    private val service by lazy { App.httpClient.getService(ArticleApi::class.java) }

    /**
     * 首页banner
     * 1分钟缓存
     */
    suspend fun getBanner(): List<Banner> {
        var banners = LitePal.where("expired > ?", "${System.currentTimeMillis()}")
            .find(Banner::class.java)

        if (banners.isEmpty()) {
            LitePal.deleteAll(
                Banner::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            banners = service.getBanners()
            banners.forEach {
                it.expired = System.currentTimeMillis() + 60 * 1000L
                it.save()
            }
        }

        return banners
    }

    /**
     * 首页置顶文章
     * 1分钟缓存
     */
    suspend fun getTopArticles(): List<Article> {
        var articles = LitePal.where(
            "type = 1 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Article::class.java)

        if (articles.isEmpty()) {
            LitePal.deleteAll(
                Article::class.java,
                "type = 1 and expired < ?",
                "${System.currentTimeMillis()}"
            )

            articles = service.getTopArticles()
            articles.forEach {
                it.expired = System.currentTimeMillis() + 60 * 1000L
                it.save()
            }
        }

        return articles
    }

    /**
     * 首页文章列表
     * 第一页1分钟缓存
     */
    suspend fun getArticles(pageNo: Int): Page<Article> {
        return if (pageNo == 0) {
            var articles = LitePal.where(
                "type = 0 and expired > ?",
                "${System.currentTimeMillis()}"
            ).find(Article::class.java)

            if (articles.isEmpty()) {
                LitePal.deleteAll(
                    Article::class.java,
                    "type = 0 and expired < ?",
                    "${System.currentTimeMillis()}"
                )

                articles = service.getArticles(pageNo).datas
                articles.forEach {
                    it.expired = System.currentTimeMillis() + 60 * 1000L
                    it.save()
                }
            }
            Page(0, articles, articles.size, false, -1, articles.size, -1)
        } else service.getArticles(pageNo)
    }

    /**
     * 知识体系下的文章
     */
    suspend fun getKnowledgeArticles(pageNo: Int, cid: Int): Page<Article> =
        service.getKnowledgeArticles(pageNo, cid)

    /**
     * 微信公众号历史文章
     */
    suspend fun getWeChatArticle(id: Int, pageNo: Int): Page<Article> =
        service.getWeChatArticle(id, pageNo)

    /**
     * 微信公众号搜索文章
     */
    suspend fun searchWeChatArticle(id: Int, pageNo: Int, keyword: String): Page<Article> =
        service.searchWeChatArticle(id, pageNo, keyword)

    /**
     * 最新项目文章
     */
    suspend fun getNewProject(pageNo: Int): Page<Article> = service.getNewProject(pageNo)

    /**
     * 项目文章，从1开始
     */
    suspend fun getProject(pageNo: Int, cid: Int): Page<Article> =
        service.getProject(pageNo, cid)

    /**
     * 收藏文章列表
     */
    suspend fun getCollections(pageNo: Int): Page<Article> = service.getCollections(pageNo)

    /**
     * 收藏站内文章
     */
    suspend fun collectInner(id: Int): String = service.collectInner(id)

    /**
     * 收藏站外文章
     */
    suspend fun collectOuter(): Article = service.collectOuter()
}