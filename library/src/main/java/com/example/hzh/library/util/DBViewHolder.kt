package com.example.hzh.library.util

import android.view.View
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseViewHolder
import com.example.hzh.library.R

/**
 * Create by hzh on 2019/09/12.
 */
class DBViewHolder(view: View) : BaseViewHolder(view) {

    fun getBinding(): ViewDataBinding =
        itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ViewDataBinding
}