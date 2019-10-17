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

    private val service by lazy { App.httpClient.getService(CategoryApi::class.java) }

    suspend fun getKnowledgeTree(): List<Category> {
        var categoryList = LitePal.where(
            "tag = 1 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Category::class.java)

        if (categoryList.isEmpty()) {
            LitePal.deleteAll(
                Category::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            categoryList = service.getKnowledgeTree()
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
            it.children = LitePal.where("parentChapterId = ?", "${it.categoryId}")
                .find(Category::class.java)
        }

        return categoryList
    }

    suspend fun getWeChatAuthors(): List<Category> {
        var categoryList = LitePal.where(
            "tag = 2 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Category::class.java)

        if (categoryList.isEmpty()) {
            LitePal.deleteAll(
                Category::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            categoryList = service.getWeChatAuthors()
            categoryList.forEach { cate ->
                cate.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                cate.tag = 2
                cate.save()
            }
        }

        return categoryList
    }

    suspend fun getProjectTree(): List<Category> {
        var categoryList = LitePal.where(
            "tag = 3 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Category::class.java)

        if (categoryList.isEmpty()) {
            LitePal.deleteAll(
                Category::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            categoryList = service.getProjectTree()
            categoryList.forEach { cate ->
                cate.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                cate.tag = 3
                cate.save()
            }
        }

        return categoryList
    }
}