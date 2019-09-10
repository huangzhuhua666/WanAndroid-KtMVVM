package com.example.hzh.ktmvvm.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Create by hzh on 2019/09/10.
 */
class PageAdapter(private val list: List<Fragment>, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val fragment = super.instantiateItem(container, position) as Fragment
//        fm.beginTransaction().run {
//            show(fragment)
//            commitAllowingStateLoss()
//        }
//        return fragment
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        fm.beginTransaction().run {
//            hide(list[position])
//            commitAllowingStateLoss()
//        }
//    }
}