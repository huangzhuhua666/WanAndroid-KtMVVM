package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.ProjectPageAdapter
import com.example.hzh.ktmvvm.viewmodel.ProjectVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * Create by hzh on 2019/09/10.
 */
class ProjectFragment : BaseFragment() {

    companion object {

        fun newInstance(): ProjectFragment = ProjectFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_project

    private val mProjectVM by lazy { obtainVM(ProjectVM::class.java) }

    override fun initView() {
        mProjectVM.context = mContext

        tabLayout?.let {
            it.setupWithViewPager(vpContent)
            it.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    p0?.run { vpContent.currentItem = position }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {

                }

                override fun onTabReselected(p0: TabLayout.Tab?) {

                }
            })
        }
    }

    override fun initListener() {
        mProjectVM.treeList.observe(this, Observer { tree ->
            vpContent?.let { it.adapter = ProjectPageAdapter(tree, childFragmentManager) }
        })
    }

    override fun initData() {
        mProjectVM.getProjectTree()
    }
}