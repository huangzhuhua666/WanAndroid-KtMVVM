package com.example.hzh.ktmvvm.data.model

import android.text.TextUtils
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Banner
import com.example.hzh.ktmvvm.data.bean.Page
import com.example.hzh.ktmvvm.data.repository.ArticleRepository
import com.example.hzh.ktmvvm.util.OperateCallback

/**
 * Create by hzh on 2019/9/29.
 */
class ArticleModel {

    private val repo by lazy { ArticleRepository.getInstance() }

    /**
     * 获取banner
     */
    suspend fun getBanner(): List<Banner> = repo.getBanner()

    /**
     * 获取首页文章
     * @param pageNo pageNo为0时，需要获取置顶文章，整合返回
     */
    suspend fun getHomeArticle(pageNo: Int): Page<Article> = repo.run {
        when (pageNo) {
            0 -> getArticles(pageNo).apply {
                datas = getTopArticles().plus(datas)
                datas.forEach { it.oCollect.set(it.collect) }
            }
            else -> getArticles(pageNo)
        }
    }

    /**
     * 获取知识体系文章
     * @param pageNo 页码从0开始
     */
    suspend fun getKnowledgeArticles(pageNo: Int, cid: Int): Page<Article> =
        repo.getKnowledgeArticles(pageNo, cid)

    /**
     * 获取微信公众号文章
     * @param id 公众号id
     * @param pageNo 页码从1开始
     */
    suspend fun getWeChatArticle(id: Int, pageNo: Int): Page<Article> =
        repo.getWeChatArticle(id, pageNo)

    /**
     * 获取项目分类的文章
     * @param pageNo 最新项目页码从0开始、其他项目分类页码从1开始
     * @param cid cid为-1时，用最新项目接口
     */
    suspend fun getProjectArticle(pageNo: Int, cid: Int): Page<Article> = when (cid) {
        -1 -> repo.getNewProject(pageNo)
        else -> repo.getProject(pageNo, cid)
    }

    /**
     * 收藏文章列表
     * @param pageNo 页码从0开始
     */
    suspend fun getCollections(pageNo: Int): Page<Article> =
        repo.getCollections(pageNo).apply { datas.forEach { it.collect = true } }

    /**
     * 收藏站内文章
     * @param id 文章id
     */
    suspend fun collectInner(id: Int) = repo.collectInner(id)

    /**
     * 收藏站外文章
     * @param article
     * @param callback
     */
    suspend fun collectOuter(article: Article, callback: OperateCallback<Article>) {
        article.run {
            if (TextUtils.isEmpty(title)) {
                callback.onInputIllegal(R.string.please_input_article_title)
                return
            }

            if (TextUtils.isEmpty(link)) {
                callback.onInputIllegal(R.string.please_input_article_link)
                return
            }

            callback.onPreOperate()

            val params = mutableMapOf("title" to title, "link" to link)
            if (!TextUtils.isEmpty(author)) params["author"] = author

            repo.collectOuter(params).let { callback.onCallback(it) }
        }
    }

    /**
     * 文章列表取消收藏
     * @param id 文章id
     */
    suspend fun unCollectArticleList(id: Int) = repo.unCollectArticleList(id)

    /**
     * 我的收藏页面取消收藏
     * @param id 文章在列表中的id
     * @param originId 文章原本的id。收藏的站外文章originId为-1
     */
    suspend fun unCollectMyCollection(id: Int, originId: Int) =
        repo.unCollectMyCollection(id, originId)

    /**
     * 搜索，支持多个关键词、空格分开
     * @param pageNo 页码，从0开始
     * @param keyword 关键词
     */
    suspend fun search(pageNo: Int, keyword: String): Page<Article> = repo.search(pageNo, keyword)
}