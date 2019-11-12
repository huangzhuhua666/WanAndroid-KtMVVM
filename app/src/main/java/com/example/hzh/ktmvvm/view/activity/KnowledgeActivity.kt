package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.databinding.ActivityKnowledgeBinding
import com.example.hzh.ktmvvm.view.fragment.KnowledgePageFragment
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.viewmodel.BaseVM
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_knowledge.*

/**
 * Create by hzh on 2019/09/18.
 */
@Suppress("UNCHECKED_CAST")
class KnowledgeActivity : BaseActivity<ActivityKnowledgeBinding, BaseVM>() {

    companion object {

        fun open(activity: Activity, title: String, category: ArrayList<Category>) {
            activity.startActivity(Intent(activity, KnowledgeActivity::class.java).apply {
                putExtras(bundleOf("title" to title, "category" to category))
            })
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_knowledge

    override val mTitleView: View?
        get() = llTitle

    private var title by DelegateExt.notNullSingleValue<String>()

    private var category by DelegateExt.notNullSingleValue<List<Category>>()

    override fun onGetBundle(bundle: Bundle) {
        bundle.let {
            title = it.getString("title", "")
            category = it.getSerializable("category") as List<Category>
        }
    }

    override fun initView() {
        mBinding.title = title

        vpContent?.adapter = SimplePageAdapter(
            supportFragmentManager,
            lifecycle,
            category.size
        ) { KnowledgePageFragment.newInstance(category[it].categoryId) }

        TabLayoutMediator(tabLayout, vpContent) { tab, position ->
            tab.text = HtmlCompat.fromHtml(
                category[position].name,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }.attach()
    }

    override fun initListener() {
        btnBack.setOnClickListener { finish() }
    }

    override fun initData() {

    }
}