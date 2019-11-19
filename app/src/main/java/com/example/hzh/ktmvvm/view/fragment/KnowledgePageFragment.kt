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
import com.example.hzh.ktmvvm.viewmodel.KnowledgeVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.http.APIException
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.android.synthetic.main.base_refresh_list.*
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/19.
 */
class KnowledgePageFragment : WanFragment<BaseRefreshListBinding, KnowledgeVM>() {

    companion object {

        fun newInstance(id: Int): KnowledgePageFragment = KnowledgePageFragment().apply {
            arguments = bundleOf("id" to id)
        }
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: KnowledgeVM?
        get() = obtainVM(KnowledgeVM::class.java)

    private val mAdapter by lazy { SimpleBindingAdapter<Article>(R.layout.item_article) }

    private var cid by Delegates.notNull<Int>()

    override fun initView() {
        mBinding.baseVM = mViewModel

        baseList.adapter = mAdapter
    }

    override fun initListener() {
        LiveEventBus.get("auth").observe(viewLifecycleOwner, Observer {
            // 登录消息，刷新文章列表
            mViewModel?.getInitData(false)
        })

        mViewModel?.run {
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

    override fun onGetBundle(bundle: Bundle) {
        bundle.let { cid = it.getInt("id") }
    }

    override fun initData() {
        mViewModel?.let {
            it.cid = cid
            it.getInitData(false)
        }
    }

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        AuthActivity.open(mContext)
    }
}