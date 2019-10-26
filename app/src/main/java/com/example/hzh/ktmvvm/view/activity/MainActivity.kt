package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.view.GravityCompat
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.databinding.ActivityMainBinding
import com.example.hzh.ktmvvm.view.fragment.HomeFragment
import com.example.hzh.ktmvvm.view.fragment.ProjectFragment
import com.example.hzh.ktmvvm.view.fragment.WeChatAuthorFragment
import com.example.hzh.ktmvvm.view.fragment.KnowledgeFragment
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.toast
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding, BaseVM>() {

    override val mLayoutId: Int
        get() = R.layout.activity_main

    override val mTitleView: View?
        get() = llTitle

    private val titles by lazy { resources.getStringArray(R.array.main_title) }

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
                R.id.collect -> {
                    if (App.isLogin) CollectionActivity.open(mContext)
                    else AuthActivity.open(mContext, 1)
                }
                R.id.todo -> toast(R.string.todo)
                R.id.about -> toast(R.string.about)
                R.id.logout -> toast(R.string.logout)
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        indicator.setOnTabChangedListener { mBinding.title = titles[it] }
    }

    override fun initData() {
        mBinding.title = titles[0]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
    }
}
