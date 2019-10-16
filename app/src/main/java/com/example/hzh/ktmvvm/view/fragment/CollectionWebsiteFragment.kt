package com.example.hzh.ktmvvm.view.fragment

import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.BaseRefreshListBinding
import com.example.hzh.ktmvvm.viewmodel.CollectionVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.base_refresh_list.*

/**
 * Create by hzh on 2019/9/26.
 */
class CollectionWebsiteFragment : BaseFragment<BaseRefreshListBinding, CollectionVM>() {

    companion object {

        fun newInstance(): CollectionWebsiteFragment = CollectionWebsiteFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.base_refresh_list

    override val mViewModel: CollectionVM?
        get() = obtainVM(CollectionVM::class.java)

    override fun initView() {
        mBinding.baseVM = mViewModel

        refreshLayout.setEnableLoadMore(false)
    }

    override fun initListener() {

    }

    override fun initData() {

    }
}