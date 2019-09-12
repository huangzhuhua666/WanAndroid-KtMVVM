package com.example.hzh.ktmvvm.adapter

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.hzh.ktmvvm.BR
import com.example.hzh.ktmvvm.data.model.ArticleBean
import com.example.hzh.ktmvvm.databinding.ItemArticleBinding
import com.example.hzh.library.R
import com.example.hzh.library.util.DBViewHolder

/**
 * Create by hzh on 2019/09/12.
 */
class ArticleAdapter(layoutResId: Int) :
    BaseQuickAdapter<ArticleBean, DBViewHolder>(layoutResId) {

    override fun convert(helper: DBViewHolder, item: ArticleBean) {
        helper.getBinding().run {
            setVariable(BR.article, item)
            executePendingBindings()
        }
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View =
        DataBindingUtil.inflate<ItemArticleBinding>(
            mLayoutInflater,
            layoutResId,
            parent,
            false
        ).let { it.root.apply { setTag(R.id.BaseQuickAdapter_databinding_support, it) } }
}