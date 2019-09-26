package com.example.hzh.ktmvvm.view.fragment

import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.viewmodel.HomeVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionArticleFragment : BaseFragment() {

    companion object {

        fun newInstance(): CollectionArticleFragment = CollectionArticleFragment()
    }

    override val layoutId: Int
        get() = R.layout.base_refresh_list

    private val mCollectionVM by lazy { obtainVM(HomeVM::class.java) }

    override fun initView() {
        (mBinding as BaseRefreshListBinding).baseVM = mCollectionVM
    }

    override fun initListener() {

    }

    override fun initData() {

    }
}