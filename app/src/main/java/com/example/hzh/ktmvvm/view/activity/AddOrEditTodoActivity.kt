package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.base.WanActivity
import com.example.hzh.ktmvvm.data.bean.Todo
import com.example.hzh.ktmvvm.databinding.ActivityAddOrEditTodoBinding
import com.example.hzh.ktmvvm.viewmodel.TodoVM
import com.example.hzh.ktmvvm.widget.TimePicker
import com.example.hzh.library.extension.filterFastClickListener
import com.example.hzh.library.extension.formatDate
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.extension.yyyy_MM_dd
import com.example.hzh.library.http.APIException
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/11/20.
 */
class AddOrEditTodoActivity : WanActivity<ActivityAddOrEditTodoBinding, TodoVM>() {

    companion object {

        fun open(activity: Activity, todo: Todo = Todo()) = activity.let {
            it.startActivity(Intent(it, AddOrEditTodoActivity::class.java).apply {
                putExtras(bundleOf("todo" to todo))
            })
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_add_or_edit_todo

    override val mTitleView: View?
        get() = mBinding.llTitle

    override val mViewModel: TodoVM?
        get() = obtainVM(TodoVM::class.java)

    override val isClickHideKeyboard: Boolean
        get() = true

    private var todo by Delegates.notNull<Todo>()

    private val mTimePicker by lazy {
        TimePicker.getPicker(
            mContext,
            getString(R.string.please_choose_todo_complete_time)
        ) { date, _ ->
            todo.dateStr = date?.time?.formatDate(yyyy_MM_dd) ?: todo.dateStr
            mBinding.todo = todo
        }
    }

    override fun onGetBundle(bundle: Bundle) {
        todo = bundle.getSerializable("todo") as Todo
    }

    override fun initView() {
        mBinding.todo = todo
    }

    override fun initListener() {
        LiveEventBus.get("save_todo").observe(mContext, Observer { finish() })

        mBinding.let {
            it.btnBack.filterFastClickListener { finish() }

            it.btnSave.filterFastClickListener { mViewModel?.addOrEditTodo(todo) }

            it.btnDate.filterFastClickListener { mTimePicker.show() }

            // 生活标签
            it.cbLife.filterFastClickListener { _ ->
                todo.type = if (todo.type == 1) 0 else 1
                it.todo = todo
            }

            // 工作标签
            it.cbWork.filterFastClickListener { _ ->
                todo.type = if (todo.type == 2) 0 else 2
                it.todo = todo
            }

            // 学习标签
            it.cbStudy.filterFastClickListener { _ ->
                todo.type = if (todo.type == 3) 0 else 3
                it.todo = todo
            }

            // 娱乐标签
            it.cbEntertainment.filterFastClickListener { _ ->
                todo.type = if (todo.type == 4) 0 else 4
                it.todo = todo
            }

            // 优先级高
            it.cbHigh.filterFastClickListener { _ ->
                todo.priority = if (todo.priority == 1) 0 else 1
                it.todo = todo
            }

            // 优先级中
            it.cbMedium.filterFastClickListener { _ ->
                todo.priority = if (todo.priority == 2) 0 else 2
                it.todo = todo
            }

            // 优先级低
            it.cbLow.filterFastClickListener { _ ->
                todo.priority = if (todo.priority == 3) 0 else 3
                it.todo = todo
            }
        }
    }

    override fun initData() {

    }

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        AuthActivity.open(mContext)
    }
}