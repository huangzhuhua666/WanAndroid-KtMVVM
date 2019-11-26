package com.example.hzh.ktmvvm.data.bean

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.hzh.library.extension.formatDate
import com.example.hzh.library.extension.yyyy_MM_dd
import java.io.Serializable

/**
 * Create by hzh on 2019/11/19.
 */

class TodoHead(val dateStr: String) : AbstractExpandableItem<Todo>(), MultiItemEntity {

    override fun getLevel(): Int = 0

    override fun getItemType(): Int = 0
}

class Todo : Serializable, MultiItemEntity {

    var completeDate = -1L
        set(value) {
            field = value ?: -1L
        }

    var completeDateStr = ""
    var content = ""
    var date = System.currentTimeMillis()
    var dateStr = System.currentTimeMillis().formatDate(yyyy_MM_dd)
    var id = -1
    var priority = 0
    var status = 0
    var title = ""
    var type = 0
    var userId = -1

    override fun getItemType(): Int = 1
}