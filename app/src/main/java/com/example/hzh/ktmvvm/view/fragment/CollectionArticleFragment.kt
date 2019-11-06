package com.example.hzh.ktmvvm.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanFragment
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.view.activity.AuthActivity
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.CollectionVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SingleBindingAdapter
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.http.APIException
import kotlinx.android.synthetic.main.base_refresh_list.*
import kotlinx.android.synthetic.main.base_refresh_list.refreshLayout

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionArticleFragment private constructor() :
    WanFragment<BaseRefreshListBinding, CollectionVM>() {

    companion object {

        fun newInstance(): CollectionArticleFragment = CollectionArticleFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: CollectionVM?
        get() = obtainVM(CollectionVM::class.java)

    private val mAdapter by lazy { SingleBindingAdapter<Article>(R.layout.item_article) }

    override fun initView() {
        mBinding.baseVM = mViewModel

        baseList.adapter = mAdapter
    }

    override fun initListener() {
        mViewModel?.run {
            articleList.observe(viewLifecycleOwner, Observer { articleList ->
                when (isLoadMore) {
                    false -> {
                        mAdapter.setNewData(articleList)
                        refreshLayout.finishRefresh()
                    }
                    true -> {
                        mAdapter.addData(articleList)
                        refreshLayout.finishLoadMore()
                    }
                }
            })
        }

        mAdapter.mPresenter = object : ItemClickPresenter<Article> {
            override fun onItemClick(view: View, item: Article) {
                when (view.id) {
                    R.id.cvRoot -> WebActivity.open(mContext, item.link, item.title) // 浏览文章
                    R.id.btnCollect -> {
                        if (App.isLogin) mViewModel?.unCollect(item) // 收藏
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