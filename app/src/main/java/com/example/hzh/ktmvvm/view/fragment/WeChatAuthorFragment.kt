package com.example.hzh.ktmvvm.view.fragment

import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.FragmentWechatAuthorBinding
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
import com.example.hzh.library.adapter.SimplePageAdapter
import com.example.hzh.library.extension.addTextChangedListener
import com.example.hzh.library.extension.filterFastClickListener
import com.example.hzh.library.fragment.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Create by hzh on 2019/09/10.
 */
class WeChatAuthorFragment : BaseFragment<FragmentWechatAuthorBinding, WeChatAuthorVM>() {

    override val mLayoutId: Int
        get() = R.layout.fragment_wechat_author

    override val mViewModel: WeChatAuthorVM? by viewModels()

    override fun initView() {
        mBinding.wechatVM = mViewModel
    }

    override fun initListener() {
        mViewModel?.run {
            authorList.observe(viewLifecycleOwner) {
                mBinding.vpContent.adapter =
                    SimplePageAdapter(childFragmentManager, lifecycle, it.size) { position ->
                        WeChatAuthorPageFragment.newInstance(it[position].categoryId)
                    }

                TabLayoutMediator(mBinding.tabLayout, mBinding.vpContent) { tab, position ->
                    tab.text =
                        HtmlCompat.fromHtml(it[position].name, HtmlCompat.FROM_HTML_MODE_LEGACY)
                }.attach()
            }

            mBinding.etSearch.addTextChangedListener {
                afterTextChanged { keyword.value = it?.toString() }
            }
        }

        mBinding.btnClear.filterFastClickListener { mBinding.etSearch.setText("") }
    }

    override fun initData() {
        mViewModel?.getWeChatAuthors()
    }
}