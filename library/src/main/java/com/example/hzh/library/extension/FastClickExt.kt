package com.example.hzh.library.extension

import android.view.View
import com.example.hzh.library.R

/**
 * Create by hzh on 2019/11/26.
 */

/**
 * 给view添加上次触发点击事件的时间
 */
private var View.triggerLastTime: Long
    get() = if (getTag(R.id.triggerLastTimeKey) != null) getTag(R.id.triggerLastTimeKey) as Long else 0
    set(value) = setTag(R.id.triggerLastTimeKey, value)

/**
 * 给view添加点击事件屏蔽时间间隔
 */
private var View.triggerDelay: Long
    get() = if (getTag(R.id.triggerDelayKey) != null) getTag(R.id.triggerDelayKey) as Long else -1
    set(value) = setTag(R.id.triggerDelayKey, value)

private fun View.isFastClick(): Boolean = System.currentTimeMillis().let {
    val isFastClick = it - triggerLastTime <= triggerDelay
    triggerLastTime = it
    isFastClick
}

/**
 * 防快速点击
 * @param delay 默认600ms间隔
 * @param action
 */
fun View.filterFastClickListener(delay: Long = 600L, action: (View) -> Unit) {
    triggerDelay = delay
    setOnClickListener { if (!isFastClick()) action(this) }
}