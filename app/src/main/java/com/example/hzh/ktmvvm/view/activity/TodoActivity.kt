package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.base.WanActivity
import com.example.hzh.ktmvvm.databinding.ActivityTodoBinding
import com.example.hzh.ktmvvm.view.fragment.TodoListFragment
import com.example.hzh.library.viewmodel.BaseVM
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Create by hzh on 2019/11/19.
 */
class TodoActivity : WanActivity<ActivityTodoBinding, BaseVM>() {

    companion object {

        fun open(activity: Activity) =
            activity.let { it.startActivity(Intent(it, TodoActivity::class.java)) }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_todo

    override val mTitleView: View?
        get() = mBinding.llTitle

    private val titleList by lazy {
        listOf(
            getString(R.string.no_complete),
            getString(R.string.completed)
        )
    }

    override fun initView() {
        mBinding.run {
            vpContent.adapter = SimplePageAdapter(
                supportFragmentManager, lifecycle,
                2
            ) { TodoListFragment.newInstance(it) }

            TabLayoutMediator(tabLayout, vpContent) { tab, position ->
                tab.text = titleList[position]
            }.attach()
        }
    }

    override fun initListener() {
        mBinding.run {
            btnBack.setOnClickListener { finish() }

            btnAdd.setOnClickListener { AddOrEditTodoActivity.open(mContext) }
        }
    }

    override fun initData() {

    }
}