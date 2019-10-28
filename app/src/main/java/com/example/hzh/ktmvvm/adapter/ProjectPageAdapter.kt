package com.example.hzh.ktmvvm.adapter

import android.content.Context
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.view.fragment.ProjectPageFragment

/**
 * Create by hzh on 2019/9/21.
 */
class ProjectPageAdapter(
    private val context: Context,
    private val data: List<Category>,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList by lazy { mutableListOf<Fragment>() }

    init {
        data.forEach { fragmentList.add(ProjectPageFragment.newInstance(it.categoryId)) }
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = data.size

    override fun getPageTitle(position: Int): CharSequence? =
        if (data[position].categoryId == -1) context.getString(R.string.fresh_project)
        else HtmlCompat.fromHtml(data[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)
}