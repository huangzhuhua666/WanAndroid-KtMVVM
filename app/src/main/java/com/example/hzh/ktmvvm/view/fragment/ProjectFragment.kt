package com.example.hzh.ktmvvm.view.fragment

import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.databinding.FragmentProjectBinding
import com.example.hzh.ktmvvm.viewmodel.ProjectVM
import com.example.hzh.library.fragment.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Create by hzh on 2019/09/10.
 */
class ProjectFragment : BaseFragment<FragmentProjectBinding, ProjectVM>() {

    companion object {

        fun newInstance(): ProjectFragment = ProjectFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_project

    override val mViewModel: ProjectVM? by viewModels()

    override fun initView() {

    }

    override fun initListener() {
        mViewModel?.run {
            treeList.observe(viewLifecycleOwner, Observer { tree ->
                mBinding.vpContent.adapter = SimplePageAdapter(
                    childFragmentManager,
                    lifecycle,
                    tree.size
                ) { ProjectPageFragment.newInstance(tree[it].categoryId) }

                TabLayoutMediator(mBinding.tabLayout, mBinding.vpContent) { tab, position ->
                    tab.text =
                        if (tree[position].categoryId == -1) getString(R.string.fresh_project)
                        else HtmlCompat.fromHtml(
                            tree[position].name,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                }.attach()
            })
        }
    }

    override fun initData() {
        mViewModel?.getProjectTree()
    }
}