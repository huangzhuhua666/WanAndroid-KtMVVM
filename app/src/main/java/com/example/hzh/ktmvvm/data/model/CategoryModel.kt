package com.example.hzh.ktmvvm.data.model

import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.data.repository.CategoryRepository

/**
 * Create by hzh on 2019/10/17.
 */
class CategoryModel {

    private val repo by lazy { CategoryRepository.getInstance() }

    suspend fun getKnowledgeTree(): List<Category> = repo.getKnowledgeTree()

    suspend fun getWeChatAuthors(): List<Category> = repo.getWeChatAuthors()

    suspend fun getProjectTree(): List<Category> = listOf(Category()).plus(repo.getProjectTree())
}