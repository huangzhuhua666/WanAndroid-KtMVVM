package com.example.hzh.ktmvvm.diff

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback

/**
 * Create by hzh on 2019/11/11.
 */
class StringDiffCallback(newList: List<String>) : BaseQuickDiffCallback<String>(newList) {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}