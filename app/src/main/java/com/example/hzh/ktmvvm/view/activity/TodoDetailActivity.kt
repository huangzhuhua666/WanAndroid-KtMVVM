package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.base.WanActivity
import com.example.hzh.ktmvvm.data.bean.Todo
import com.example.hzh.ktmvvm.databinding.ActivityTodoDetailBinding
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.ktmvvm.viewmodel.TodoVM
import com.example.hzh.library.extension.filterFastClickListener
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/11/25.
 */
class TodoDetailActivity : WanActivity<ActivityTodoDetailBinding, TodoVM>() {

    companion object {

        fun open(activity: Activity, todo: Todo) = activity.let {
            it.startActivity(Intent(it, TodoDetailActivity::class.java).apply {
                putExtras(bundleOf("todo" to todo))
            })
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_todo_detail

    override val mTitleView: View?
        get() = mBinding.llTitle

    override val mViewModel: TodoVM? by viewModels()

    private var todo by Delegates.notNull<Todo>()

    override fun onGetBundle(bundle: Bundle) {
        todo = bundle.getSerializable("todo") as Todo
    }

    override fun initView() {
        mBinding.todo = todo
    }

    override fun initListener() {
        mBinding.let {
            it.btnBack.filterFastClickListener { finish() }

            it.btnDelete.filterFastClickListener {
                LiveEventBus.get(Event.TODO_DELETE).post(todo)
                finish()
            }
        }
    }

    override fun initData() {

    }
}