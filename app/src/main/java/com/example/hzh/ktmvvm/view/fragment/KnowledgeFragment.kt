package com.example.hzh.ktmvvm.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Category
import com.example.hzh.ktmvvm.databinding.FragmentKnowledgeBinding
import com.example.hzh.ktmvvm.view.activity.KnowledgeActivity
import com.example.hzh.ktmvvm.viewmodel.KnowledgeVM
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment

/**
 * Create by hzh on 2019/09/10.
 */
class KnowledgeFragment : BaseFragment<FragmentKnowledgeBinding, KnowledgeVM>() {

    companion object {

        fun newInstance(): KnowledgeFragment = KnowledgeFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_knowledge

    override val mViewModel: KnowledgeVM?
        get() = obtainVM(KnowledgeVM::class.java)

    private val mAdapter by lazy {
        SimpleBindingAdapter<Category>(R.layout.item_knowledge).also {
            it.mPresenter = object : ItemClickPresenter<Category> {
                override fun onItemClick(view: View, item: Category, position: Int) {
                    KnowledgeActivity.open(mContext, item.name, item.children as ArrayList)
                }
            }
        }
    }

    override fun initView() {
        mBinding.run {
            baseVM = mViewModel

            rvSystem.adapter = mAdapter
        }
    }

    override fun initListener() {
        mViewModel?.treeList?.observe(mContext, Observer { mAdapter.setNewData(it) })
        mBinding.refreshLayout.setOnRefreshListener { mViewModel?.getSystemTree(true) }
    }

    override fun initData() {
        mViewModel?.getSystemTree(false)
    }
}