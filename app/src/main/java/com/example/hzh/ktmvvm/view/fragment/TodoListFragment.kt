package com.example.hzh.ktmvvm.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.base.WanFragment
import com.example.hzh.ktmvvm.data.bean.Todo
import com.example.hzh.ktmvvm.databinding.FragmentTodoListBinding
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.ktmvvm.util.TodoDiffCallback
import com.example.hzh.ktmvvm.view.activity.AddOrEditTodoActivity
import com.example.hzh.ktmvvm.view.activity.AuthActivity
import com.example.hzh.ktmvvm.view.activity.TodoDetailActivity
import com.example.hzh.ktmvvm.viewmodel.TodoVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.MultiTypeBindingAdapter
import com.example.hzh.library.extension.filterFastClickListener
import com.example.hzh.library.http.APIException
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/11/22.
 */
class TodoListFragment : WanFragment<FragmentTodoListBinding, TodoVM>() {

    companion object {

        fun newInstance(status: Int): TodoListFragment = TodoListFragment().apply {
            arguments = bundleOf("status" to status)
        }
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_todo_list

    override val mViewModel: TodoVM? by viewModels()

    private var status by Delegates.notNull<Int>()

    private val mAdapter by lazy {
        MultiTypeBindingAdapter(
            mapOf(
                0 to R.layout.item_todo_head,
                1 to R.layout.item_todo_child
            )
        )
    }

    override fun initView() {
        mBinding.run {
            todoVM = mViewModel

            rvTodo.adapter = mAdapter
        }
    }

    override fun initListener() {
        LiveEventBus.get(Event.TODO_SAVE).observe(viewLifecycleOwner, Observer {
            //添加、编辑需要刷新列表
            if (status == 0) mViewModel?.getInitData(false)
        })
        LiveEventBus.get(Event.TODO_STATUS, Int::class.java).observe(viewLifecycleOwner, Observer {
            if (status == it) mViewModel?.getInitData(false) // 刷新列表
        })
        LiveEventBus.get(Event.TODO_DELETE, Todo::class.java).observe(viewLifecycleOwner, Observer {
            if (status == 1) mViewModel?.deleteTodo(it) // 删除已完成项
        })

        mBinding.run {
            // 生活标签
            cbLife.filterFastClickListener {
                mViewModel?.type?.value = if (mViewModel?.type?.value!! == 1) 0 else 1
                mViewModel?.getInitData(false)
            }

            // 工作标签
            cbWork.filterFastClickListener {
                mViewModel?.type?.value = if (mViewModel?.type?.value!! == 2) 0 else 2
                mViewModel?.getInitData(false)
            }

            // 学习标签
            cbStudy.filterFastClickListener {
                mViewModel?.type?.value = if (mViewModel?.type?.value!! == 3) 0 else 3
                mViewModel?.getInitData(false)
            }

            // 娱乐标签
            cbEntertainment.filterFastClickListener {
                mViewModel?.type?.value = if (mViewModel?.type?.value!! == 4) 0 else 4
                mViewModel?.getInitData(false)
            }

            // 优先级高
            cbHigh.filterFastClickListener {
                mViewModel?.priority?.value = if (mViewModel?.priority?.value!! == 1) 0 else 1
                mViewModel?.getInitData(false)
            }

            // 优先级中
            cbMedium.filterFastClickListener {
                mViewModel?.priority?.value = if (mViewModel?.priority?.value!! == 2) 0 else 2
                mViewModel?.getInitData(false)
            }

            // 优先级低
            cbLow.filterFastClickListener {
                mViewModel?.priority?.value = if (mViewModel?.priority?.value!! == 3) 0 else 3
                mViewModel?.getInitData(false)
            }
        }

        mViewModel?.run {
            todoList.observe(viewLifecycleOwner, Observer { data ->
                mAdapter.setNewDiffData(TodoDiffCallback(data))
                mAdapter.expandAll()
            })
        }

        mAdapter.mPresenter = object : ItemClickPresenter<MultiItemEntity>() {
            override fun onItemClick(view: View, item: MultiItemEntity, position: Int) {
                if (item is Todo) {
                    when (view.id) {
                        R.id.clRoot -> {
                            // 编辑、详情
                            if (item.status == 0) AddOrEditTodoActivity.open(mContext, item)
                            else TodoDetailActivity.open(mContext, item)
                        }
                        R.id.btnStatus -> mViewModel?.updateTodoStatus(item) // 更新Todo状态
                        R.id.btnDelete -> mViewModel?.deleteTodo(item) // 删除Todo
                    }
                }
            }
        }
    }

    override fun onGetBundle(bundle: Bundle) {
        bundle.let { status = it.getInt("status") }
    }

    override fun initData() {
        mViewModel?.let {
            it.status = status
            it.getInitData(false)
        }
    }

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        AuthActivity.open(mContext)
    }
}