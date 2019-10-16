package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.ProjectPageAdapter
import com.example.hzh.ktmvvm.databinding.FragmentProjectBinding
import com.example.hzh.ktmvvm.viewmodel.ProjectVM
import com.example.hzh.library.extension.addOnTabSelectedListener
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * Create by hzh on 2019/09/10.
 */
class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectVM>() {

    companion object {

        fun newInstance(): ProjectFragment = ProjectFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_project

    override val mViewModel: ProjectVM?
        get() = obtainVM(ProjectVM::class.java)

    override fun initView() {
        tabLayout?.run {
            setupWithViewPager(vpContent)
            addOnTabSelectedListener {
                onTabSelected { it?.run { vpContent?.currentItem = position } }
            }
        }
    }

    override fun initListener() {
        mViewModel?.run {
            treeList.observe(mContext, Observer { tree ->
                vpContent?.adapter = ProjectPageAdapter(mContext, tree, childFragmentManager)
            })
        }
    }

    override fun initData() {
        mViewModel?.getProjectTree()
    }
}