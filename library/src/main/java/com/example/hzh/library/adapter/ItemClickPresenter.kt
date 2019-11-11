package com.example.hzh.library.adapter

import android.view.View

/**
 * Create by hzh on 2019/11/5.
 */
interface ItemClickPresenter<in T> {

    fun onItemClick(view: View, item: T)

    fun onLongClick(view: View, item: T): Boolean = false
}