package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
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
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding, BaseVM>() {

    companion object {
        const val VIEW_COLLECTION = 0x10
    }

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
            ).let { fragmentList ->
                adapter = SimplePageAdapter(
                    supportFragmentManager,
                    lifecycle,
                    fragmentList.size
                ) { fragmentList[it] }
                offscreenPageLimit = fragmentList.size
            }

            indicator.viewpager = this
            isUserInputEnabled = false
        }
    }

    override fun initListener() {
        btnDrawer.setOnClickListener { drawer.openDrawer(GravityCompat.START) }

        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.collect -> {
                    if (App.isLogin) CollectionActivity.open(mContext)
                    else AuthActivity.open(mContext, VIEW_COLLECTION)
                }
                R.id.todo -> toast(R.string.todo)
                R.id.about -> toast(R.string.about)
                R.id.logout -> toast(R.string.logout)
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        indicator.setOnTabChangedListener { mBinding.title = titles[it] }

        LiveEventBus.get("auth", Boolean::class.java).observe(this, Observer {
            // TODO
        })
    }

    override fun initData() {
        mBinding.title = titles[0]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            VIEW_COLLECTION -> CollectionActivity.open(mContext)
        }
    }
}
