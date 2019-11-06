package com.example.hzh.ktmvvm.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Create by hzh on 2019/09/10.
 */
class SimplePageAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val size: Int, // 总共需要加载的fragment数量
    private val action: ((Int) -> Fragment) // 用这个来根据position加载fragment
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = size

    override fun createFragment(position: Int): Fragment = action.invoke(position)
}