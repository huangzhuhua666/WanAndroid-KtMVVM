package com.example.hzh.ktmvvm.view.fragment

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanFragment
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.databinding.FragmentHomeBinding
import com.example.hzh.ktmvvm.databinding.LayoutBannerBinding
import com.example.hzh.ktmvvm.util.ArticleDiffCallback
import com.example.hzh.ktmvvm.view.activity.AuthActivity
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.HomeVM
import com.example.hzh.ktmvvm.widget.ObsBanner
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.http.APIException
import com.jeremyliao.liveeventbus.LiveEventBus
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/10.
 */
class HomeFragment : WanFragment<FragmentHomeBinding, HomeVM>() {

    companion object {

        fun newInstance(): HomeFragment = HomeFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_home

    override val mViewModel: HomeVM?
        get() = obtainVM(HomeVM::class.java)

    private val mAdapter by lazy { SimpleBindingAdapter<Article>(R.layout.item_article) }

    private val mBannerBinding by lazy {
        DataBindingUtil.inflate<LayoutBannerBinding>(
            layoutInflater,
            R.layout.layout_banner,
            rvArticle.parent as ViewGroup,
            false
        )
    }

    private var mBanner by Delegates.notNull<ObsBanner>()

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
        LiveEventBus.get("auth").observe(viewLifecycleOwner, Observer {
            // 登录消息，刷新文章列表
            mViewModel?.getInitData(false)
        })
        LiveEventBus.get("uncollect").observe(viewLifecycleOwner, Observer {
            // 我的收藏页面取消收藏，数据可能有变化，刷新一下列表
            mViewModel?.getInitData(false)
        })

        mViewModel?.run {
            mBanner.setOnBannerListener {
                bannerList.value?.get(it)?.run { WebActivity.open(mContext, url, title) }
            }

            articleList.observe(viewLifecycleOwner, Observer { articleList ->
                mAdapter.setNewDiffData(ArticleDiffCallback(articleList))
            })
        }

        mAdapter.mPresenter = object : ItemClickPresenter<Article> {
            override fun onItemClick(view: View, item: Article, position: Int) {
                when (view.id) {
                    R.id.cvRoot -> WebActivity.open(mContext, item.link, item.title) // 浏览文章
                    R.id.btnCollect -> {
                        if (App.isLogin) mViewModel?.collectOrNot(item) // 收藏
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