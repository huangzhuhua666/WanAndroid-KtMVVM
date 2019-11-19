package com.example.hzh.library.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.hzh.library.BR
import com.example.hzh.library.R

/**
 * Create by hzh on 2019/11/5.
 */
open class SimpleBindingAdapter<T>(layoutResId: Int) :
    BaseQuickAdapter<T, DBViewHolder>(layoutResId) {

    var mPresenter: ItemClickPresenter<T>? = null

    override fun convert(helper: DBViewHolder, item: T) {
        helper.getBinding().run {
            setVariable(BR.item, item)
            setVariable(BR.position, helper.adapterPosition)
            setVariable(BR.presenter, mPresenter)
            executePendingBindings()
        }
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View =
        DataBindingUtil.inflate<ViewDataBinding>(
            mLayoutInflater,
            layoutResId,
            parent,
            false
        ).let { it.root.apply { setTag(R.id.BaseQuickAdapter_databinding_support, it) } }
}