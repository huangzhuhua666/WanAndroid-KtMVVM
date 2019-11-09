package com.example.hzh.ktmvvm.util

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.example.hzh.ktmvvm.data.bean.Website

/**
 * Create by hzh on 2019/11/9.
 */
class WebsiteDiffCallback(newList: List<Website>) : BaseQuickDiffCallback<Website>(newList) {

    override fun areItemsTheSame(oldItem: Website, newItem: Website): Boolean =
        oldItem.websiteId.compareTo(newItem.websiteId) == 0

    override fun areContentsTheSame(oldItem: Website, newItem: Website): Boolean =
        oldItem.name == newItem.name && oldItem.link == newItem.link
}