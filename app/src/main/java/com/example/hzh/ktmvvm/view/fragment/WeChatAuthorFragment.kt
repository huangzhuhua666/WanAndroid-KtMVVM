package com.example.hzh.ktmvvm.view.fragment

import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.SimplePageAdapter
import com.example.hzh.ktmvvm.databinding.FragmentWechatAuthorBinding
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
import com.example.hzh.library.extension.addTextChangedListener
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_wechat_author.*

/**
 * Create by hzh on 2019/09/10.
 */
class WeChatAuthorFragment : BaseFragment<FragmentWechatAuthorBinding, WeChatAuthorVM>() {

    companion object {

        fun newInstance(): WeChatAuthorFragment = WeChatAuthorFragment()
    }

    override val mLayoutId: Int
        get() = R.layout.fragment_wechat_author

    override val mViewModel: WeChatAuthorVM?
        get() = obtainVM(WeChatAuthorVM::class.java)

    override fun initView() {
        mBinding.wechatVM = mViewModel
    }

    override fun initListener() {
        mViewModel?.run {
            authorList.observe(viewLifecycleOwner, Observer { authorList ->
                vpContent?.adapter = SimplePageAdapter(
                    childFragmentManager,
                    lifecycle,
                    authorList.size
                ) { WeChatAuthorPageFragment.newInstance(authorList[it].categoryId) }

                TabLayoutMediator(tabLayout, vpContent) { tab, position ->
                    tab.text = HtmlCompat.fromHtml(
                        authorList[position].name,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }.attach()
            })

            etSearch.addTextChangedListener { afterTextChanged { keyword.value = it?.toString() } }
        }

        btnClear.setOnClickListener { etSearch.setText("") }
    }

    override fun initData() {
        mViewModel?.getWeChatAuthors()
    }
}