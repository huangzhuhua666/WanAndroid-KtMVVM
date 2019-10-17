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
        var categorys = LitePal.where(
            "tag = 1 and expired > ?",
            "${System.currentTimeMillis()}"
        ).find(Category::class.java)

        if (categorys.isEmpty()) {
            LitePal.deleteAll(
                Category::class.java,
                "expired < ?",
                "${System.currentTimeMillis()}"
            )

            categorys = service.getKnowledgeTree()
            categorys.forEach { cate ->
                cate.expired = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000L
                cate.tag = 1
                cate.save()
                cate.children.forEach { child ->
                    child.expired = cate.expired
                    child.save()
                }
            }
        } else categorys.forEach {
            it.children = LitePal.where("parentChapterId = ?", "${it.categoryId}")
                .find(Category::class.java)
        }

        return categorys
    }

    suspend fun getWeChatAuthors(): List<Category> {
        return service.getWeChatAuthors()
    }

    suspend fun getProjectTree(): List<Category> {
        return service.getProjectTree()
    }
}