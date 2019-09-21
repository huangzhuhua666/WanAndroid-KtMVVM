package com.example.hzh.ktmvvm.view.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.ArticleAdapter
import com.example.hzh.ktmvvm.databinding.FragmentWechatAuthorPageBinding
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_wechat_author_page.*
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/21.
 */
class WeChatAuthorPageFragment : BaseFragment() {

    companion object {

        fun newInstance(id: Int): WeChatAuthorPageFragment = WeChatAuthorPageFragment().also {
            it.arguments = bundleOf("id" to id)
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_wechat_author_page

    private val mWeChatVM by lazy { obtainVM(WeChatAuthorVM::class.java) }

    private val mAdapter by lazy { ArticleAdapter(R.layout.item_article) }

    private var cid by Delegates.notNull<Int>()

    override fun onGetBundle(bundle: Bundle) {
        bundle.let { cid = it.getInt("id") }
    }

    override fun initView() {
        (mBinding as FragmentWechatAuthorPageBinding).wechatVM = mWeChatVM

        mWeChatVM.id = cid

        rvArticle.adapter = mAdapter
    }

    override fun initListener() {
        mWeChatVM.let {
            it.articleList.observe(this, Observer { articleList ->
                when (it.isLoadMore) {
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
        mWeChatVM.getInitData()
    }
}