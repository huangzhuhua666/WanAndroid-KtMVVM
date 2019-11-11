package com.example.hzh.ktmvvm.data.model

import com.example.hzh.ktmvvm.data.repository.CacheRepository

/**
 * Create by hzh on 2019/11/11.
 */
class CacheModel {

    private val repo by lazy { CacheRepository.getInstance() }

    /**
     * 获取搜索历史
     */
    fun getHistory(): List<String>? = repo.getHistory()

    /**
     * 保存搜索历史
     * @param data
     */
    fun saveHistory(data: String) {
        // 有缓存搜索历史
        repo.getHistory()?.run {
            this as MutableList<String>
            if (!contains(data)) { // 搜索历史有该关键字
                if (size >= 10) removeAt(9) // 关键字数量满10个，删除最后一个关键字
            } else remove(data) // 搜索历史有该关键字，移位
            add(0, data) // 把该关键字提到第一位
            repo.saveHistory(this) // 保存
            return
        }
        // 无缓存搜索历史
        listOf(data).let { repo.saveHistory(it) }
    }

    /**
     * 清空搜索历史
     */
    fun cleanHistory() = repo.cleanHistory()
}