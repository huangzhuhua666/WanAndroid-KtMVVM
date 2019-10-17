package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.KnowledgeAdapter
import com.example.hzh.ktmvvm.databinding.FragmentKnowledgeBinding
import com.example.hzh.ktmvvm.viewmodel.KnowledgeVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_knowledge.*

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

    private val mAdapter by lazy { KnowledgeAdapter(R.layout.item_knowledge) }

    override fun initView() {
        rvSystem.adapter = mAdapter
    }

    override fun initListener() {
        mViewModel?.treeList?.observe(mContext, Observer { mAdapter.setNewData(it) })
    }

    override fun initData() {
        mViewModel?.getSystemTree()
    }
}