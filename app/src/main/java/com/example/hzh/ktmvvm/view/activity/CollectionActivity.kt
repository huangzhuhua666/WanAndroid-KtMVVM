package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.databinding.ActivityCollectionBinding
import com.example.hzh.ktmvvm.view.fragment.*
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.toast
import com.example.hzh.library.viewmodel.BaseVM
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * Create by hzh on 2019/9/25.
 */
class CollectionActivity : BaseActivity<ActivityCollectionBinding, BaseVM>() {

    companion object {

        fun open(activity: Activity) {
            activity.startActivity(Intent(activity, CollectionActivity::class.java))
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_collection

    override val mTitleView: View?
        get() = llTitle

    private val titleList = listOf(getString(R.string.article), getString(R.string.website))

    override fun initView() {
        listOf(
            CollectionArticleFragment.newInstance(),
            CollectionWebsiteFragment.newInstance()
        ).let { fragmentList ->
            vpContent?.adapter = SimplePageAdapter(
                supportFragmentManager, lifecycle,
                fragmentList.size
            ) { fragmentList[it] }
        }

        TabLayoutMediator(tabLayout, vpContent) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    override fun initListener() {
        btnBack.setOnClickListener { finish() }

        btnAdd.setOnClickListener { toast("TODO") }
    }

    override fun initData() {

    }
}