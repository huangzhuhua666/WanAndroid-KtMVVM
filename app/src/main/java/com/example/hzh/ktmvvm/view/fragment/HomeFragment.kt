package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.ArticleAdapter
import com.example.hzh.ktmvvm.databinding.FragmentHomeBinding
import com.example.hzh.ktmvvm.viewmodel.HomeVM
import com.example.hzh.library.extension.setListener
import com.example.hzh.library.extension.toast
import com.example.hzh.library.fragment.BaseFragment
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Create by hzh on 2019/09/10.
 */
class HomeFragment : BaseFragment() {

    companion object {

        fun newInstance(): HomeFragment = HomeFragment()
    }

    private val mHomeVM by lazy { ViewModelProviders.of(this)[HomeVM::class.java] }

    private val mAdapter by lazy { ArticleAdapter(R.layout.item_article) }

    override val layoutId: Int
        get() = R.layout.fragment_home

    override fun initView() {
        (mBinding as FragmentHomeBinding).homeVM = mHomeVM
        banner?.let {
            lifecycle.addObserver(it)
            it.isPlayOnStart = false
            it.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
            it.setIndicatorGravity(BannerConfig.RIGHT)
        }

        rvArticle.adapter = mAdapter
    }

    override fun initListener() {
        banner.setOnBannerListener {
            mHomeVM.bannerList.value?.get(it)?.url?.run { mContext.toast(this) }
        }

        refreshLayout.setListener {
            onRefresh {
                mHomeVM.getInitData()
            }
            onLoadMore { mHomeVM.loadArticles() }
        }

        mHomeVM.let {
            it.articleList.observe(this, Observer { articleList ->
                if (!it.isLoadMore) {
                    mAdapter.setNewData(articleList)
                    refreshLayout.finishRefresh()
                } else {
                    mAdapter.addData(articleList)
                    refreshLayout.finishLoadMore()
                }
            })
        }
    }

    override fun initData() {
        mHomeVM.getInitData()
    }
}