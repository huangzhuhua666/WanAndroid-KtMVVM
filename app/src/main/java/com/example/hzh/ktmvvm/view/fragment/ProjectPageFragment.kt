package com.example.hzh.ktmvvm.view.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.ArticleAdapter
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.viewmodel.ProjectVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.base_refresh_list.*
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/9/21.
 */
class ProjectPageFragment : BaseFragment() {

    companion object {

        fun newInstance(id: Int): ProjectPageFragment = ProjectPageFragment().also {
            it.arguments = bundleOf("id" to id)
        }
    }

    override val layoutId: Int
        get() = R.layout.base_refresh_list

    private val mProjectVM by lazy { obtainVM(ProjectVM::class.java) }

    private val mAdapter by lazy { ArticleAdapter(R.layout.item_article) }

    private var cid by Delegates.notNull<Int>()

    override fun onGetBundle(bundle: Bundle) {
        bundle.let { cid = it.getInt("id") }
    }

    override fun initView() {
        (mBinding as BaseRefreshListBinding).baseVM = mProjectVM

        mProjectVM.cid = cid

        rvArticle.adapter = mAdapter
    }

    override fun initListener() {
        mProjectVM.let {
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
        mProjectVM.getInitData()
    }
}