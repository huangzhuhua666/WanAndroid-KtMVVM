package com.example.hzh.ktmvvm.view.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.base.WanFragment
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.util.ArticleDiffCallback
import com.example.hzh.ktmvvm.view.activity.AuthActivity
import com.example.hzh.ktmvvm.view.activity.WebActivity
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.http.APIException
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/21.
 */
class WeChatAuthorPageFragment : WanFragment<BaseRefreshListBinding, WeChatAuthorVM>() {

    companion object {

        fun newInstance(id: Int): WeChatAuthorPageFragment = WeChatAuthorPageFragment().apply {
            arguments = bundleOf("id" to id)
        }
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: WeChatAuthorVM?
        get() = obtainVM(WeChatAuthorVM::class.java)

    private val mAdapter by lazy { SimpleBindingAdapter<Article>(R.layout.item_article) }

    private var cid by Delegates.notNull<Int>()

    override fun initView() {
        mBinding.run {
            baseVM = mViewModel

            baseList.adapter = mAdapter
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
                        if (App.isLogin) mViewModel?.collectOrNot(item) // 收藏
                        else AuthActivity.open(mContext)
                    }
                }
            }
        }
    }

    override fun onGetBundle(bundle: Bundle) {
        bundle.let { cid = it.getInt("id") }
    }

    override fun initData() {
        mViewModel?.let {
            it.id.value = cid
            it.getInitData(false)
        }
    }

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        AuthActivity.open(mContext)
    }
}