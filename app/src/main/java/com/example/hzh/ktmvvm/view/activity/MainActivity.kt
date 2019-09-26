package com.example.hzh.ktmvvm.view.activity

import android.view.View
import androidx.core.view.GravityCompat
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.databinding.ActivityMainBinding
import com.example.hzh.ktmvvm.view.fragment.HomeFragment
import com.example.hzh.ktmvvm.view.fragment.ProjectFragment
import com.example.hzh.ktmvvm.view.fragment.WeChatAuthorFragment
import com.example.hzh.ktmvvm.view.fragment.KnowledgeFragment
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.toast
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
            ).also { adapter = SimplePageAdapter(it, supportFragmentManager) }
            indicator.viewpager = this
            isScroll = false
        }
    }

    override fun initListener() {
        btnDrawer.setOnClickListener { drawer.openDrawer(GravityCompat.START) }

        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.collect -> CollectionActivity.open(mContext)
                R.id.todo -> toast(R.string.todo)
                R.id.about -> toast(R.string.about)
                R.id.logout -> toast(R.string.logout)
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        indicator.setOnTabChangedListener { binding.title = titles[it] }
    }

    override fun initData() {
        binding.title = titles[0]
    }
}
