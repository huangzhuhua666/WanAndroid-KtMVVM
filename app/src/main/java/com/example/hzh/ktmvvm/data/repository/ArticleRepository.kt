package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.bean.Page
import com.example.hzh.ktmvvm.data.network.ArticleApi
import com.example.hzh.library.http.APIException
import com.example.hzh.library.http.NetConfig
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
        // 获取缓存
        var banners = LitePal.where("expired > ?", "${System.currentTimeMillis()}")
            .find(Banner::class.java)

        if (banners.isEmpty()) { // 缓存过期
            // 删除过期缓存
            LitePal.deleteAll(
                Banner::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            // 从网络获取数据
            banners = service.getBanners()
            // 缓存
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
        // 获取缓存
        var articles = LitePal.where(
            "type = 1 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Article::class.java)

        if (articles.isEmpty()) { // 缓存过期
            LitePal.deleteAll(
                Article::class.java,
                "type = 1 and expired < ?",
                "${System.currentTimeMillis()}"
            )

            // 从网络获取数据
            articles = service.getTopArticles()
            // 缓存
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
     * @param pageNo 页码从0开始
     */
    suspend fun getArticles(pageNo: Int): Page<Article> {
        return if (pageNo == 0) {
            // 获取缓存
            var articles = LitePal.where(
                "type = 0 and expired > ?",
                "${System.currentTimeMillis()}"
            ).find(Article::class.java)

            if (articles.isEmpty()) { // 缓存过期
                LitePal.deleteAll(
                    Article::class.java,
                    "type = 0 and expired < ?",
                    "${System.currentTimeMillis()}"
                )

                // 从网络获取数据
                articles = service.getArticles(pageNo).datas
                // 缓存
                articles.forEach {
                    it.expired = System.currentTimeMillis() + 60 * 1000L
                    it.save()
                }
            }
            // 包装数据
            Page(0, articles, articles.size, false, -1, articles.size, -1)
        } else service.getArticles(pageNo)
    }

    /**
     * 知识体系下的文章
     * @param pageNo 页码从0开始
     * @param cid 知识体系分类
     */
    suspend fun getKnowledgeArticles(pageNo: Int, cid: Int): Page<Article> =
        service.getKnowledgeArticles(pageNo, cid)

    /**
     * 微信公众号历史文章
     * @param id 公众号id
     * @param pageNo 页码从1开始
     */
    suspend fun getWeChatArticle(id: Int, pageNo: Int): Page<Article> =
        service.getWeChatArticle(id, pageNo)

    /**
     * 微信公众号搜索文章
     * @param id 公众号id
     * @param pageNo 页码从1开始
     */
    suspend fun searchWeChatArticle(id: Int, pageNo: Int, keyword: String): Page<Article> =
        service.searchWeChatArticle(id, pageNo, keyword)

    /**
     * 最新项目文章
     * @param pageNo 页码从0开始
     */
    suspend fun getNewProject(pageNo: Int): Page<Article> = service.getNewProject(pageNo)

    /**
     * 项目分类文章
     * @param pageNo 页码从1开始
     * @param cid 项目分类id
     */
    suspend fun getProject(pageNo: Int, cid: Int): Page<Article> =
        service.getProject(pageNo, cid)

    /**
     * 收藏文章列表
     * @param pageNo 页码从0开始
     */
    suspend fun getCollections(pageNo: Int): Page<Article> = service.getCollections(pageNo)

    /**
     * 收藏站内文章
     * @param id 文章id
     */
    suspend fun collectInner(id: Int) = try {
        service.collectInner(id)
    } catch (e: APIException) {
        if (e.code == NetConfig.CODE_NO_RESPONSE_BODY) { // 操作成功了后台还是返回null，不是我的锅啊
            Article().apply {
                collect = true
                updateAll("articleId = ?", "$id")
            }
        } else throw e
    }

    /**
     * 收藏站外文章
     */
    suspend fun collectOuter(): Article = service.collectOuter()

    /**
     * 文章列表取消收藏
     * @param id 文章id
     */
    suspend fun unCollectArticleList(id: Int) = try {
        service.unCollectArticleList(id)
    } catch (e: APIException) {
        if (e.code == NetConfig.CODE_NO_RESPONSE_BODY) { // 操作成功了后台还是返回null，不是我的锅啊
            Article().apply {
                setToDefault("collect")
                updateAll("articleId = ?", "$id")
            }
        } else throw e
    }

    /**
     * 我的收藏页面取消收藏
     * @param id 文章在列表中的id
     * @param originId 文章原本的id。收藏的站外文章originId为-1
     */
    suspend fun unCollectMyCollection(id: Int, originId: Int) = try {
        service.unCollectMyCollection(id, originId)
    } catch (e: APIException) {
        if (e.code == NetConfig.CODE_NO_RESPONSE_BODY) { // 操作成功了后台还是返回null，不是我的锅啊
            Article().apply {
                setToDefault("collect")
                updateAll("articleId = ?", "$originId")
            }
        } else throw e
    }
}