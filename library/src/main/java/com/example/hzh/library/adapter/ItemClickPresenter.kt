package com.example.hzh.library.adapter

import android.os.SystemClock
import android.view.View

/**
 * Create by hzh on 2019/11/5.
 */
abstract class ItemClickPresenter<in T> {

    var isFastClick = false
    private val mMills = LongArray(2)
    private var mLastClickView: View? = null

    open fun onItemClick(view: View, item: T, position: Int) {
        mMills.let {
            System.arraycopy(it, 1, it, 0, 1)
            it[1] = SystemClock.uptimeMillis()

            isFastClick = if (view == mLastClickView) it[1] - it[0] <= 600
            else {
                mLastClickView = view
                false
            }
        }
    }

    open fun onLongClick(view: View, item: T, position: Int): Boolean = false
}