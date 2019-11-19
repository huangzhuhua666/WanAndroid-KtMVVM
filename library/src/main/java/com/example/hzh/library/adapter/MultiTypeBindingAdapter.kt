package com.example.hzh.library.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.hzh.library.BR
import com.example.hzh.library.R

/**
 * Create by hzh on 2019/11/13.
 */
open class MultiTypeBindingAdapter(map: Map<Int, Int>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, DBViewHolder>(listOf()) {

    var mPresenter: ItemClickPresenter<MultiItemEntity>? = null

    init {
        map.forEach { addItemType(it.key, it.value) }
    }

    override fun convert(helper: DBViewHolder, item: MultiItemEntity) {
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