package com.example.hzh.ktmvvm.data.repository

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.data.bean.Page
import com.example.hzh.ktmvvm.data.bean.Todo
import com.example.hzh.ktmvvm.data.network.TodoApi
import com.example.hzh.library.http.APIException
import com.example.hzh.library.http.NetConfig

/**
 * Create by hzh on 2019/11/19.
 */
class TodoRepository private constructor() {

    companion object {

        fun getInstance(): TodoRepository = HOLDER.instance
    }

    private object HOLDER {
        val instance = TodoRepository()
    }

    private val service by lazy { App.httpClient.getService<TodoApi>() }

    /**
     * 获取TodoList
     * @param pageNo 页码，从1开始
     * @param status 状态：0 -> 未完成，1 -> 已完成
     * @param type 类型，0默认全部展示
     * @param priority 优先级，0默认全部展示
     */
    suspend fun getTodoList(
        pageNo: Int,
        status: Int,
        type: Int,
        priority: Int
    ): Page<Todo> = service.getTodoList(pageNo, status, type, priority)

    /**
     * 新增一个Todo
     * @param params title 标题
     *
     *               content(非必传) 新增详情
     *
     *               date(不传默认当天，建议传) 预计完成时间
     *
     *               type(不传默认0) Todo类型，大于0的整数
     *
     *               priority(不传默认0) 优先级，大于0的整数
     */
    suspend fun addTodo(params: Map<String, Any>) = service.addTodo(params)

    /**
     * 编辑Todo
     * @param id id
     * @param params title 标题
     *
     *               content(非必传) 新增详情
     *
     *               date 预订完成时间
     *
     *               type(不传默认0) Todo类型，大于0的整数
     *
     *               priority(不传默认0) 优先级，大于0的整数
     */
    suspend fun editTodo(id: Int, params: Map<String, Any>) = service.editTodo(id, params)

    /**
     * 更新Todo状态
     * @param id id
     * @param status 状态：0 -> 未完成，1 -> 已完成
     */
    suspend fun updateTodoStatus(id: Int, status: Int) = service.updateTodoStatus(id, status)

    /**
     * 删除Todo
     * @param id id
     */
    suspend fun deleteTodo(id: Int) = try {
        service.deleteTodo(id)
    } catch (e: APIException) {
        if (e.code == NetConfig.CODE_NO_RESPONSE_BODY) "" // 操作成功了后台还是返回null，不是我的锅啊
        else throw e
    }
}