package com.example.hzh.ktmvvm.adapter

import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hzh.ktmvvm.data.model.CategoryBean
import com.example.hzh.ktmvvm.view.fragment.WeChatAuthorPageFragment

/**
 * Create by hzh on 2019/9/21.
 */
class WeChatAuthorPageAdapter(
    private val data: List<CategoryBean>,
    fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList by lazy { mutableListOf<Fragment>() }

    init {
        data.forEach { fragmentList.add(WeChatAuthorPageFragment.newInstance(it.id)) }
    }

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = data.size

    override fun getPageTitle(position: Int): CharSequence? =
        HtmlCompat.fromHtml(data[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)
}