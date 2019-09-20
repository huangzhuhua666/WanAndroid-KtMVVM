package com.example.hzh.ktmvvm.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Create by hzh on 2019/09/10.
 */
class BottomNavigationPageAdapter(
    private val fragment: List<Fragment>,
    private val fm: FragmentManager
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragment[position]

    override fun getCount(): Int = fragment.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        fm.beginTransaction().run {
            show(fragment)
            commitAllowingStateLoss()
        }
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        fm.beginTransaction().run {
            hide(fragment[position])
            commitAllowingStateLoss()
        }
    }
}