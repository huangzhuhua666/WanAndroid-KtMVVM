package com.example.hzh.ktmvvm.view.activity

import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.PageAdapter
import com.example.hzh.ktmvvm.view.fragment.HomeFragment
import com.example.hzh.ktmvvm.view.fragment.ProjectFragment
import com.example.hzh.ktmvvm.view.fragment.PublicAuthorFragment
import com.example.hzh.ktmvvm.view.fragment.SystemFragment
import com.example.hzh.library.activity.BaseActivity
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.setFitsSystemWindows
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override val isStatusBarDarkFont: Boolean
        get() = true

    override fun initView() {
        immersionBar { setFitsSystemWindows() }

        vpContent?.run {
            listOf(
                HomeFragment.newInstance(),
                SystemFragment.newInstance(),
                PublicAuthorFragment.newInstance(),
                ProjectFragment.newInstance()
            ).also {
                adapter = PageAdapter(it, supportFragmentManager)
            }
            indicator.setViewPager(this)
            isScroll = false
        }
    }

    override fun initListener() {

    }

    override fun initData() {

    }
}
