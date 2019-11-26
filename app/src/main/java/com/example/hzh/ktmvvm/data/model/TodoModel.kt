package com.example.hzh.ktmvvm.data.model

import android.text.TextUtils
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Todo
import com.example.hzh.ktmvvm.data.bean.TodoHead
import com.example.hzh.ktmvvm.data.repository.TodoRepository
import com.example.hzh.ktmvvm.util.OperateCallback

/**
 * Create by hzh on 2019/11/19.
 */
class TodoModel {

    private val repo by lazy { TodoRepository.getInstance() }

    /**
     * 获取TodoList
     * @param pageNo 页码，从1开始
     * @param status 状态：0 -> 未完成，1 -> 已完成
     * @param type 类型，0默认全部展示
     * @param priority 优先级，0默认全部展示
     */
    suspend fun getTodoList(
        dataMap: MutableMap<String, TodoHead> = mutableMapOf(),
        pageNo: Int,
        status: Int,
        type: Int,
        priority: Int,
        callback: OperateCallback<Boolean>
    ): MutableMap<String, TodoHead> = repo.getTodoList(pageNo, status, type, priority).run {
        datas.forEach {
            if (dataMap.containsKey(it.dateStr)) dataMap[it.dateStr]?.addSubItem(it) // 已存在该日期分类
            else dataMap[it.dateStr] = TodoHead(it.dateStr).also { head -> head.addSubItem(it) }
        }
        dataMap.values.forEach { it.isExpanded = false } // 更新展开状态，不然无法正常展开
        callback.onCallback(over) // 标记是否结束
        dataMap
    }

    /**
     * 新增一个Todo
     */
    suspend fun addTodo(todo: Todo, callback: OperateCallback<Todo>) = todo.run {
        if (TextUtils.isEmpty(title)) {
            callback.onInputIllegal(R.string.please_input_title)
            return
        }

        callback.onPreOperate()

        val params = mutableMapOf(
            "title" to title, "content" to content,
            "date" to dateStr, "type" to type, "priority" to priority
        )
        repo.addTodo(params).let { callback.onCallback(it) }
    }

    /**
     * 编辑Todo
     */
    suspend fun editTodo(todo: Todo, callback: OperateCallback<Todo>) = todo.run {
        if (TextUtils.isEmpty(title)) {
            callback.onInputIllegal(R.string.please_input_title)
            return
        }

        callback.onPreOperate()

        val params = mutableMapOf(
            "title" to title, "content" to content,
            "date" to dateStr, "type" to type, "priority" to priority
        )
        repo.editTodo(id, params).let { callback.onCallback(it) }
    }

    /**
     * 更新Todo状态
     */
    suspend fun updateTodoStatus(
        dataMap: MutableMap<String, TodoHead>,
        todo: Todo
    ): MutableMap<String, TodoHead> =
        repo.updateTodoStatus(todo.id, if (todo.status == 0) 1 else 0).let {
            // 从列表中移除这一项
            dataMap[todo.dateStr]!!.removeSubItem(todo)
            // 不含子项，移除父项
            if (!dataMap[todo.dateStr]!!.hasSubItem()) dataMap.remove(todo.dateStr)
            dataMap.values.forEach { it.isExpanded = false } // 更新展开状态，不然无法正常展开
            dataMap
        }

    /**
     * 删除Todo
     */
    suspend fun deleteTodo(
        dataMap: MutableMap<String, TodoHead>,
        todo: Todo
    ): MutableMap<String, TodoHead> =
        repo.deleteTodo(todo.id).let {
            // 从列表中移除这一项
            dataMap[todo.dateStr]?.run { removeSubItem(subItems.first { sub -> sub.id == todo.id }) }
            // 不含子项，移除父项
            if (!dataMap[todo.dateStr]!!.hasSubItem()) dataMap.remove(todo.dateStr)
            dataMap.values.forEach { it.isExpanded = false } // 更新展开状态，不然无法正常展开
            dataMap
        }
}