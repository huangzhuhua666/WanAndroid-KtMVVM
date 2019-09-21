package com.example.hzh.ktmvvm.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.KnowledgePageAdapter
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.ktmvvm.databinding.ActivityKnowledgeBinding
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.DelegateExt
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_knowledge.*

/**
 * Create by hzh on 2019/09/18.
 */
@Suppress("UNCHECKED_CAST")
class KnowledgeActivity : BaseActivity() {

    companion object {

        fun open(ctx: Context, title: String, category: ArrayList<CategoryBean>) {
            ctx.startActivity(Intent(ctx, KnowledgeActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putString("title", title)
                    putSerializable("category", category)
                })
            })
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_knowledge

    override val titleView: View?
        get() = llTitle

    private var title by DelegateExt.notNullSingleValue<String>()

    private var category by DelegateExt.notNullSingleValue<List<CategoryBean>>()

    private val binding by lazy { mBinding as ActivityKnowledgeBinding }

    override fun onGetBundle(bundle: Bundle) {
        bundle.let {
            title = it.getString("title", "")
            category = it.getSerializable("category") as List<CategoryBean>
        }
    }

    override fun initView() {
        binding.title = title

        tabLayout?.let {
            it.setupWithViewPager(vpContent)
            it.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    p0?.run { vpContent.currentItem = position }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {

                }

                override fun onTabReselected(p0: TabLayout.Tab?) {

                }
            })
        }

        vpContent?.let { it.adapter = KnowledgePageAdapter(category, supportFragmentManager) }
    }

    override fun initListener() {

    }

    override fun initData() {

    }
}