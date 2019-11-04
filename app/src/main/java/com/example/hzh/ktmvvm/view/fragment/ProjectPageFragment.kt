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
class ProjectPageFragment : BaseFragment<BaseRefreshListBinding, ProjectVM>() {

    companion object {

        fun newInstance(id: Int): ProjectPageFragment = ProjectPageFragment().apply {
            arguments = bundleOf("id" to id)
        }
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: ProjectVM?
        get() = obtainVM(ProjectVM::class.java)

    private val mAdapter by lazy { ArticleAdapter(R.layout.item_article) }

    private var cid by Delegates.notNull<Int>()

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
}