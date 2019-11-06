package com.example.hzh.ktmvvm.data.model

import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.repository.CategoryRepository

/**
 * Create by hzh on 2019/10/17.
 */
class CategoryModel {

    private val repo by lazy { CategoryRepository.getInstance() }

    /**
     * 获取知识体系数据
     */
    suspend fun getKnowledgeTree(): List<Category> = repo.getKnowledgeTree()

    /**
     * 获取微信公众号
     */
    suspend fun getWeChatAuthors(): List<Category> = repo.getWeChatAuthors()

    /**
     * 获取项目分类
     */
    suspend fun getProjectTree(): List<Category> = listOf(Category()).plus(repo.getProjectTree())
}