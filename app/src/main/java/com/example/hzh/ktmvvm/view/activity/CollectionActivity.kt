package com.example.hzh.ktmvvm.view.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.view.fragment.*
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.addOnTabSelectedListener
import com.example.hzh.library.extension.toast
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * Create by hzh on 2019/9/25.
 */
class CollectionActivity : BaseActivity() {

    companion object {

        fun open(ctx: Context) {
            ctx.startActivity(Intent(ctx, CollectionActivity::class.java))
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_collection

    override val titleView: View?
        get() = llTitle

    override fun initView() {
        tabLayout?.run {
            setupWithViewPager(vpContent)
            addOnTabSelectedListener {
                onTabSelected { it?.run { vpContent?.currentItem = position } }
            }
        }

        vpContent?.run {
            listOf(
                CollectionArticleFragment.newInstance(),
                CollectionWebsiteFragment.newInstance()
            ).also {
                adapter = SimplePageAdapter(it, supportFragmentManager).apply {
                    titles = listOf(getString(R.string.article), getString(R.string.website))
                }
            }
        }
    }

    override fun initListener() {
        btnBack.setOnClickListener { finish() }

        btnAdd.setOnClickListener { toast("TODO") }
    }

    override fun initData() {

    }
}