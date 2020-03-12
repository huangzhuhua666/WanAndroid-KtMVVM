package com.example.hzh.ktmvvm.view.fragment

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanFragment
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.databinding.FragmentHomeBinding
import com.example.hzh.ktmvvm.databinding.LayoutBannerBinding
import com.example.hzh.ktmvvm.diff.ArticleDiffCallback
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.ktmvvm.view.activity.AuthActivity
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.HomeVM
import com.example.hzh.ktmvvm.widget.ObsBanner
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.http.APIException
import com.jeremyliao.liveeventbus.LiveEventBus
import com.youth.banner.BannerConfig
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/10.
 */
class HomeFragment : WanFragment<FragmentHomeBinding, HomeVM>() {

    override val mLayoutId: Int
        get() = R.layout.fragment_home

    override val mViewModel: HomeVM? by viewModels()

    private val mAdapter by lazy { SimpleBindingAdapter<Article>(R.layout.item_article) }

    private val mBannerBinding by lazy {
        DataBindingUtil.inflate<LayoutBannerBinding>(
            layoutInflater,
            R.layout.layout_banner,
            mBinding.rvArticle.parent as ViewGroup,
            false
        )
    }

    private var mBanner by Delegates.notNull<ObsBanner>()

    private val mBannerFastClick = LongArray(2)

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
            mBinding.rvArticle.adapter = it
            it.addHeaderView(mBanner)
        }
    }

    override fun initListener() {
        LiveEventBus.get(Event.AUTH).observe(viewLifecycleOwner, Observer {
            // 登录消息，刷新文章列表
            mViewModel?.getInitData(false)
        })
        LiveEventBus.get(Event.ARTICLE_CANCEL_COLLECT).observe(viewLifecycleOwner, Observer {
            // 我的收藏页面取消收藏，数据可能有变化，刷新一下列表
            mViewModel?.getInitData(false)
        })

        mViewModel?.run {
            mBanner.setOnBannerListener {
                System.arraycopy(mBannerFastClick, 1, mBannerFastClick, 0, 1)
                mBannerFastClick[1] = SystemClock.uptimeMillis()

                if (mBannerFastClick[1] - mBannerFastClick[0] > 600) // 防快击
                    bannerList.value?.get(it)?.run { WebActivity.open(mContext, url, title) }
            }

            articleList.observe(viewLifecycleOwner, Observer { articleList ->
                mAdapter.setNewDiffData(
                    ArticleDiffCallback(
                        articleList
                    )
                )
            })
        }

        mAdapter.mPresenter = object : ItemClickPresenter<Article>() {
            override fun onItemClick(view: View, item: Article, position: Int) {
                super.onItemClick(view, item, position)
                if (isFastClick) return

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