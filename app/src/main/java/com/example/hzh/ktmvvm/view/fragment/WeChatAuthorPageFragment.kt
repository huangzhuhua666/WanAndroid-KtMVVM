package com.example.hzh.ktmvvm.view.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.ArticleAdapter
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.base_refresh_list.*
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/21.
 */
class WeChatAuthorPageFragment : BaseFragment<BaseRefreshListBinding, WeChatAuthorVM>() {

    companion object {

        fun newInstance(id: Int): WeChatAuthorPageFragment = WeChatAuthorPageFragment().also {
            it.arguments = bundleOf("id" to id)
        }
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: WeChatAuthorVM?
        get() = obtainVM(WeChatAuthorVM::class.java)

    private val mAdapter by lazy { ArticleAdapter(R.layout.item_article) }

    private var cid by Delegates.notNull<Int>()

    override fun initView() {
        mBinding.baseVM = mViewModel

        rvArticle.adapter = mAdapter
    }

    override fun initListener() {
        mViewModel?.run {
            articleList.observe(this@WeChatAuthorPageFragment, Observer { articleList ->
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

    override fun onGetBundle(bundle: Bundle) {
        bundle.let { cid = it.getInt("id") }
    }

    override fun initData() {
        mViewModel?.let {
            it.id.value = cid
            it.getInitData(false)
        }
    }
}