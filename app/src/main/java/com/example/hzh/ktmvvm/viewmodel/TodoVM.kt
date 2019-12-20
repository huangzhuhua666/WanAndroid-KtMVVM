package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Todo
import com.example.hzh.ktmvvm.data.bean.TodoHead
import com.example.hzh.ktmvvm.data.model.TodoModel
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.ktmvvm.util.OperateCallback
import com.example.hzh.library.viewmodel.BaseVM
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/11/19.
 */
class TodoVM : BaseVM() {

    private val todoModel by lazy { TodoModel() }

    /**
     * 状态：0 -> 未完成，1 -> 已完成
     */
    var status by Delegates.notNull<Int>()

    /**
     * 类型，0默认全部展示
     */
    val type = MutableLiveData(0)

    /**
     * 优先级，0默认全部展示
     */
    val priority = MutableLiveData(0)

    private val _todoMap = MutableLiveData<MutableMap<String, TodoHead>>()

    /**
     * 用MutableList，不然列表展开的时候可能会出错
     */
    private val _todoList = MutableLiveData<MutableList<MultiItemEntity>>()
    val todoList: LiveData<MutableList<MultiItemEntity>> = _todoList

    override fun getInitData(isRefresh: Boolean) {
        pageNo = 1
        _isShowLoading.value = !isRefresh
        doOnIO(
            tryBlock = {
                todoModel.getTodoList(
                    pageNo = pageNo,
                    status = status,
                    type = type.value!!,
                    priority = priority.value!!,
                    callback = object : OperateCallback<Boolean> {
                        override fun onCallback(data: Boolean) = _isOver.postValue(data)
                    }).let {
                    _todoMap.postValue(it) // 更新map，方便后续分类操作
                    _todoList.postValue(it.values.toMutableList())
                }
            },
            finallyBlock = {
                _isShowLoading.value = false
                isFinish.value = true
            }
        )
    }

    override fun loadData() {
        super.loadData()
        doOnIO(
            tryBlock = {
                todoModel.getTodoList(
                    _todoMap.value!!,
                    pageNo,
                    status,
                    type.value!!,
                    priority.value!!,
                    object : OperateCallback<Boolean> {
                        override fun onCallback(data: Boolean) = _isOver.postValue(data)
                    }).let {
                    _todoMap.postValue(it) // 更新map，方便后续分类操作
                    _todoList.postValue(it.values.toMutableList())
                }
            },
            catchBlock = { --pageNo },
            finallyBlock = { isFinish.value = true }
        )
    }

    fun addOrEditTodo(todo: Todo) {
        doOnIO(
            tryBlock = {
                val callback = object : OperateCallback<Todo> {
                    override fun onInputIllegal(tip: Int) = _toastTip.postValue(tip)

                    override fun onPreOperate() = _isShowLoading.postValue(true)

                    override fun onCallback(data: Todo) {
                        LiveEventBus.get(Event.TODO_SAVE).post(true)
                        _toastTip.postValue(R.string.save_success)
                    }
                }

                if (todo.id == -1) todoModel.addTodo(todo, callback) // 添加TODO
                else todoModel.editTodo(todo, callback) // 编辑TODO
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    /**
     * 更新Todo状态
     */
    fun updateTodoStatus(todo: Todo) {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = {
                todoModel.updateTodoStatus(_todoMap.value!!, todo).let {
                    _todoMap.postValue(it)  // 更新map，方便后续分类操作
                    _todoList.postValue(it.values.toMutableList())
                    LiveEventBus.get(Event.TODO_STATUS).post(if (todo.status == 0) 1 else 0)
                }
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }

    /**
     * 删除Todo
     */
    fun deleteTodo(todo: Todo) {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = {
                todoModel.deleteTodo(_todoMap.value!!, todo).let {
                    _todoMap.postValue(it)  // 更新map，方便后续分类操作
                    _todoList.postValue(it.values.toMutableList())
                }
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }
}