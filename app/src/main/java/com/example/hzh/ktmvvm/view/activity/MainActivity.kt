package com.example.hzh.ktmvvm.view.activity

import android.view.View
import androidx.core.view.GravityCompat
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.BottomNavigationPageAdapter
import com.example.hzh.ktmvvm.databinding.ActivityMainBinding
import com.example.hzh.ktmvvm.view.fragment.HomeFragment
import com.example.hzh.ktmvvm.view.fragment.ProjectFragment
import com.example.hzh.ktmvvm.view.fragment.WeChatAuthorFragment
import com.example.hzh.ktmvvm.view.fragment.KnowledgeFragment
import com.example.hzh.library.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override val titleView: View?
        get() = llTitle

    private val titles by lazy { resources.getStringArray(R.array.main_title) }

    private val binding by lazy { mBinding as ActivityMainBinding }

    override fun initView() {
        vpContent?.run {
            listOf(
                HomeFragment.newInstance(),
                KnowledgeFragment.newInstance(),
                WeChatAuthorFragment.newInstance(),
                ProjectFragment.newInstance()
            ).also { adapter = BottomNavigationPageAdapter(it, supportFragmentManager) }
            indicator.setViewPager(this)
            isScroll = false
        }
    }

    override fun initListener() {
        btnDrawer.setOnClickListener { drawer.openDrawer(GravityCompat.START) }

        indicator.setOnTabChangedListener { binding.title = titles[it] }
    }

    override fun initData() {
        binding.title = titles[0]
    }
}
