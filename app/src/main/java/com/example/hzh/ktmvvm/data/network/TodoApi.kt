package com.example.hzh.ktmvvm.data.network

import com.example.hzh.ktmvvm.data.bean.Page
import com.example.hzh.ktmvvm.data.bean.Todo
import retrofit2.http.*

/**
 * Create by hzh on 2019/11/19.
 */
interface TodoApi {

    /**
     * 获取TodoList
     * @param pageNo 页码，从1开始
     * @param status 状态：0 -> 未完成，1 -> 已完成
     * @param type 类型，0默认全部展示
     * @param priority 优先级，0默认全部展示
     */
    @GET("/lg/todo/v2/list/{pageNo}/json")
    suspend fun getTodoList(
        @Path("pageNo") pageNo: Int,
        @Query("status") status: Int,
        @Query("type") type: Int,
        @Query("priority") priority: Int
    ): Page<Todo>

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
    @JvmSuppressWildcards
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodo(@FieldMap params: Map<String, Any>): Todo

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
    @JvmSuppressWildcards
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    suspend fun editTodo(
        @Path("id") id: Int,
        @FieldMap params: Map<String, Any>
    ): Todo

    /**
     * 更新Todo状态
     * @param id id
     * @param status 状态：0 -> 未完成，1 -> 已完成
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun updateTodoStatus(@Path("id") id: Int, @Field("status") status: Int): Todo

    /**
     * 删除Todo
     * @param id id
     */
    @POST("/lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): String
}