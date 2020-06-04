package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.network.CategoryApi
import org.litepal.LitePal

/**
 * Create by hzh on 2019/10/17.
 */
class CategoryRepository private constructor() {

    companion object {

        fun getInstance(): CategoryRepository = Holder.mInstance
    }

    private object Holder {
        val mInstance = CategoryRepository()
    }

    private val service by lazy { App.httpClient.getService<CategoryApi>() }

    /**
     * 获取知识体系数据
     */
    suspend fun getKnowledgeTree(): List<Category> {
        // 获取缓存
        var categoryList = LitePal.where(
            "tag = 1 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Category::class.java)

        if (categoryList.isEmpty()) { // 缓存过期
            // 删除缓存
            LitePal.deleteAll(
                Category::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            // 从网络获取数据
            categoryList = service.getKnowledgeTree()
            // 缓存
            categoryList.forEach { cate ->
                cate.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                cate.tag = 1
                cate.save()
                cate.children.forEach { child ->
                    child.expired = cate.expired
                    child.save()
                }
            }
        } else categoryList.forEach {
            // 获取分类的children
            it.children = LitePal.where("parentChapterId = ?", "${it.categoryId}")
                .find(Category::class.java)
        }

        return categoryList
    }

    /**
     * 获取微信公众号
     */
    suspend fun getWeChatAuthors(): List<Category> {
        // 获取缓存
        var categoryList = LitePal.where(
            "tag = 2 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Category::class.java)

        if (categoryList.isEmpty()) { // 缓存过期
            // 删除缓存
            LitePal.deleteAll(
                Category::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            // 从网络获取数据
            categoryList = service.getWeChatAuthors()
            // 缓存
            categoryList.forEach { cate ->
                cate.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                cate.tag = 2
                cate.save()
            }
        }

        return categoryList
    }

    /**
     * 获取项目分类
     */
    suspend fun getProjectTree(): List<Category> {
        // 获取缓存
        var categoryList = LitePal.where(
            "tag = 3 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Category::class.java)

        if (categoryList.isEmpty()) { // 缓存过期
            // 删除缓存
            LitePal.deleteAll(
                Category::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            // 从网络获取数据
            categoryList = service.getProjectTree()
            // 缓存
            categoryList.forEach { cate ->
                cate.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                cate.tag = 3
                cate.save()
            }
        }

        return categoryList
    }
}