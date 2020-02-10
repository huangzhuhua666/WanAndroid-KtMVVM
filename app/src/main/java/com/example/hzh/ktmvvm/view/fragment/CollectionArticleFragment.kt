package com.example.hzh.ktmvvm.view.fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanFragment
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.util.ArticleDiffCallback
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.ktmvvm.view.activity.AuthActivity
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.CollectionVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.http.APIException
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionArticleFragment : WanFragment<BaseRefreshListBinding, CollectionVM>() {

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: CollectionVM? by viewModels()

    private val mAdapter by lazy { SimpleBindingAdapter<Article>(R.layout.item_article) }

    override fun initView() {
        mViewModel?.flag = 0

        mBinding.run {
            baseVM = mViewModel

            baseList.adapter = mAdapter
        }
    }

    override fun initListener() {
        LiveEventBus.get(Event.COLLECTION_ARTICLE_UPDATE).observe(viewLifecycleOwner, Observer {
            mViewModel?.getInitData(false)
        })

        mViewModel?.run {
            articleList.observe(viewLifecycleOwner, Observer { articleList ->
                mAdapter.setNewDiffData(ArticleDiffCallback(articleList))
            })
        }

        mAdapter.mPresenter = object : ItemClickPresenter<Article>() {
            override fun onItemClick(view: View, item: Article, position: Int) {
                super.onItemClick(view, item, position)
                if (isFastClick) return

                when (view.id) {
                    R.id.cvRoot -> WebActivity.open(mContext, item.link, item.title) // 浏览文章
                    R.id.btnCollect -> {
                        if (App.isLogin) mViewModel?.unCollect(item) // 取消收藏
                        else AuthActivity.open(mContext)
                    }
                }
            }
        }
    }

    override fun initData() {
        mViewModel?.getInitData(false)
    }

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        AuthActivity.open(mContext)
    }
}