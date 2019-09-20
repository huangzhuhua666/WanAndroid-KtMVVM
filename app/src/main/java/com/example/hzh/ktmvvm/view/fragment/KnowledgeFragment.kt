package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SystemAdapter
import com.example.hzh.ktmvvm.databinding.FragmentKnowledgeBinding
import com.example.hzh.ktmvvm.viewmodel.KnowledgeVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_knowledge.*

/**
 * Create by hzh on 2019/09/10.
 */
class KnowledgeFragment : BaseFragment() {

    companion object {

        fun newInstance(): KnowledgeFragment = KnowledgeFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_knowledge

    private val mKnowledgeVM by lazy { ViewModelProviders.of(this)[KnowledgeVM::class.java] }

    private val mAdapter by lazy { SystemAdapter(R.layout.item_knowledge) }

    override fun initView() {
        (mBinding as FragmentKnowledgeBinding).knowledgeVM = mKnowledgeVM

        rvSystem.adapter = mAdapter
    }

    override fun initListener() {
        mKnowledgeVM.treeList.observe(this, Observer { mAdapter.setNewData(it) })
    }

    override fun initData() {
        mKnowledgeVM.getSystemTree()
    }
}