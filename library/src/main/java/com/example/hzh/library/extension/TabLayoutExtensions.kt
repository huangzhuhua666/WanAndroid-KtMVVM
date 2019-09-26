package com.example.hzh.library.extension

import com.google.android.material.tabs.TabLayout

/**
 * Create by hzh on 2019/9/25.
 */
fun TabLayout.addOnTabSelectedListener(init: TabSelectedHelper.() -> Unit) {
    val listener = TabSelectedHelper()
    listener.init()
    addOnTabSelectedListener(listener)
}

private typealias TabAction = (TabLayout.Tab?) -> Unit

class TabSelectedHelper : TabLayout.OnTabSelectedListener {

    private var tabSelected: TabAction? = null
    private var tabUnselected: TabAction? = null
    private var tabReselected: TabAction? = null

    fun onTabSelected(action: TabAction) {
        tabSelected = action
    }

    fun onTabUnselected(action: TabAction) {
        tabSelected = action
    }

    fun onTabReselected(action: TabAction) {
        tabSelected = action
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        tabSelected?.invoke(p0)
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
        tabUnselected?.invoke(p0)
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
        tabReselected?.invoke(p0)
    }
}