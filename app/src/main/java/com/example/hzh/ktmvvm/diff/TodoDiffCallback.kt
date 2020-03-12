package com.example.hzh.ktmvvm.diff

import com.chad.library.adapter.base.diff.BaseQuickDiffCallback
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.hzh.ktmvvm.data.bean.Todo
import com.example.hzh.ktmvvm.data.bean.TodoHead

/**
 * Create by hzh on 2019/11/20.
 */
class TodoDiffCallback(newList: MutableList<MultiItemEntity>) :
    BaseQuickDiffCallback<MultiItemEntity>(newList) {

    override fun areItemsTheSame(oldItem: MultiItemEntity, newItem: MultiItemEntity): Boolean =
        (oldItem is TodoHead && newItem is TodoHead) || (oldItem is Todo && newItem is Todo)

    override fun areContentsTheSame(oldItem: MultiItemEntity, newItem: MultiItemEntity): Boolean =
        false
}