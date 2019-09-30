package com.example.hzh.ktmvvm.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.KnowledgePageAdapter
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.databinding.ActivityKnowledgeBinding
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.extension.addOnTabSelectedListener
import kotlinx.android.synthetic.main.activity_knowledge.*

/**
 * Create by hzh on 2019/09/18.
 */
@Suppress("UNCHECKED_CAST")
class KnowledgeActivity : BaseActivity() {

    companion object {

        fun open(ctx: Context, title: String, category: ArrayList<Category>) {
            ctx.startActivity(Intent(ctx, KnowledgeActivity::class.java).apply {
                putExtras(bundleOf("title" to title, "category" to category))
            })
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_knowledge

    override val titleView: View?
        get() = llTitle

    private var title by DelegateExt.notNullSingleValue<String>()

    private var category by DelegateExt.notNullSingleValue<List<Category>>()

    private val binding by lazy { mBinding as ActivityKnowledgeBinding }

    override fun onGetBundle(bundle: Bundle) {
        bundle.let {
            title = it.getString("title", "")
            category = it.getSerializable("category") as List<Category>
        }
    }

    override fun initView() {
        binding.title = title

        tabLayout?.run {
            setupWithViewPager(vpContent)
            addOnTabSelectedListener {
                onTabSelected { it?.run { vpContent?.currentItem = position } }
            }
        }

        vpContent?.adapter = KnowledgePageAdapter(category, supportFragmentManager)
    }

    override fun initListener() {

    }

    override fun initData() {

    }
}