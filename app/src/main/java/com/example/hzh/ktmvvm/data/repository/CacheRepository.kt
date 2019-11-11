package com.example.hzh.ktmvvm.data.repository

import android.content.Context
import androidx.core.content.edit
import com.alibaba.fastjson.JSON
import com.example.hzh.ktmvvm.app.App

/**
 * Create by hzh on 2019/11/11.
 */
class CacheRepository private constructor() {

    companion object {

        fun getInstance(): CacheRepository = Holder.mInstance
    }

    private object Holder {
        val mInstance = CacheRepository()
    }

    /**
     * 获取搜索历史
     */
    fun getHistory(): List<String>? =
        App.context.getSharedPreferences("search_history", Context.MODE_PRIVATE)
            .getString("history", "")?.let { JSON.parseArray(it, String::class.java) }

    /**
     * 保存搜索历史
     * @param data
     */
    fun saveHistory(data: List<String>) =
        App.context.getSharedPreferences("search_history", Context.MODE_PRIVATE).edit {
            putString("history", JSON.toJSONString(data))
        }

    /**
     * 清空搜索历史
     */
    fun cleanHistory() =
        App.context.getSharedPreferences("search_history", Context.MODE_PRIVATE).edit { clear() }
}