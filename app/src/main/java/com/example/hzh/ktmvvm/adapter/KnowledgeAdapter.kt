package com.example.hzh.ktmvvm.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.hzh.ktmvvm.BR
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.CategoryBean
import com.example.hzh.ktmvvm.databinding.ItemKnowledgeBinding
import com.example.hzh.ktmvvm.view.activity.KnowledgeActivity
import com.example.hzh.library.util.DBViewHolder

/**
 * Create by hzh on 2019/09/18.
 */
class SystemAdapter(layoutResId: Int) : BaseQuickAdapter<CategoryBean, DBViewHolder>(layoutResId) {

    private val mPresenter by lazy { KnowledgePresenter(mContext) }

    override fun convert(helper: DBViewHolder, item: CategoryBean) {
        helper.getBinding().run {
            setVariable(BR.category, item)
            setVariable(BR.presenter, mPresenter)
            executePendingBindings()
        }
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View =
        DataBindingUtil.inflate<ItemKnowledgeBinding>(
            mLayoutInflater,
            layoutResId,
            parent,
            false
        ).let {
            it.root.apply {
                setTag(com.example.hzh.library.R.id.BaseQuickAdapter_databinding_support, it)
            }
        }
}

class KnowledgePresenter(private val ctx: Context) {

    fun onClick(view: View, category: CategoryBean) {
        category.run {
            when (view.id) {
                R.id.cvRoot -> KnowledgeActivity.open(ctx, name, children as ArrayList)
            }
        }
    }
}