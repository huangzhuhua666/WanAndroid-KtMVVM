package com.example.hzh.ktmvvm.view.fragment

import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.viewmodel.CollectionVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionArticleFragment : BaseFragment<BaseRefreshListBinding, CollectionVM>() {

    companion object {

        fun newInstance(): CollectionArticleFragment = CollectionArticleFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: CollectionVM?
        get() = obtainVM(CollectionVM::class.java)

    override fun initView() {
        mBinding.baseVM = mViewModel
    }

    override fun initListener() {

    }

    override fun initData() {
        mViewModel?.getInitData(false)
    }
}