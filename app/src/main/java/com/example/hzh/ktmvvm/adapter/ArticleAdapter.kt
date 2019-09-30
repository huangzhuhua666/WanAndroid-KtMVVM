package com.example.hzh.ktmvvm.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.hzh.ktmvvm.BR
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.databinding.ItemArticleBinding
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.library.util.DBViewHolder

/**
 * Create by hzh on 2019/09/12.
 */
class ArticleAdapter(layoutResId: Int) : BaseQuickAdapter<Article, DBViewHolder>(layoutResId) {

    private val mPresenter by lazy { ArticlePresenter(mContext) }

    override fun convert(helper: DBViewHolder, item: Article) {
        helper.getBinding().run {
            setVariable(BR.article, item)
            setVariable(BR.presenter, mPresenter)
            executePendingBindings()
        }
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View =
        DataBindingUtil.inflate<ItemArticleBinding>(
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

class ArticlePresenter(private val ctx: Context) {

    fun onClick(view: View, article: Article) {
        article.run {
            when (view.id) {
                R.id.cvRoot -> WebActivity.open(ctx, link, title)
            }
        }
    }
}