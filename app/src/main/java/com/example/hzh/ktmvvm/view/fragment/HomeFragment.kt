package com.example.hzh.ktmvvm.view.fragment

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.ArticleAdapter
import com.example.hzh.ktmvvm.databinding.FragmentHomeBinding
import com.example.hzh.ktmvvm.databinding.LayoutBannerBinding
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.HomeVM
import com.example.hzh.ktmvvm.widget.ObsBanner
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Create by hzh on 2019/09/10.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeVM>() {

    companion object {

        fun newInstance(): HomeFragment = HomeFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_home

    override val mViewModel: HomeVM?
        get() = obtainVM(HomeVM::class.java)

    private val mAdapter by lazy { ArticleAdapter(R.layout.item_article) }

    private val mBannerBinding by lazy {
        DataBindingUtil.inflate<LayoutBannerBinding>(
            layoutInflater,
            R.layout.layout_banner,
            rvArticle,
            false
        )
    }

    private var mBanner by DelegateExt.notNullSingleValue<ObsBanner>()

    override fun initView() {
        mBinding.homeVM = mViewModel

        mBannerBinding?.let {
            it.homeVM = mViewModel
            it.lifecycleOwner = this
            mBanner = mBannerBinding.root as ObsBanner
        }

        mBanner.let {
            lifecycle.addObserver(it)
            it.isPlayOnStart = false
            it.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
            it.setIndicatorGravity(BannerConfig.RIGHT)
        }

        mAdapter.let {
            rvArticle.adapter = it
            it.addHeaderView(mBanner)
        }
    }

    override fun initListener() {
        mViewModel?.run {
            mBanner.setOnBannerListener {
                bannerList.value?.get(it)?.run { WebActivity.open(mContext, url, title) }
            }

            articleList.observe(this@HomeFragment, Observer { articleList ->
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
    }

    override fun initData() {
        mViewModel?.getInitData()
    }
}