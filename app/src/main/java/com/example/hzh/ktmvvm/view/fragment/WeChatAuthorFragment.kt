package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.WeChatAuthorPageAdapter
import com.example.hzh.ktmvvm.databinding.FragmentWechatAuthorBinding
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
import com.example.hzh.library.extension.addOnTabSelectedListener
import com.example.hzh.library.extension.addTextChangedListener
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_wechat_author.*

/**
 * Create by hzh on 2019/09/10.
 */
class WeChatAuthorFragment : BaseFragment<FragmentWechatAuthorBinding>() {

    companion object {

        fun newInstance(): WeChatAuthorFragment = WeChatAuthorFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_wechat_author

    private val mWeChatVM by lazy { obtainVM(WeChatAuthorVM::class.java) }

    override fun initView() {
        mBinding.wechatVM = mWeChatVM

        tabLayout?.run {
            setupWithViewPager(vpContent)
            addOnTabSelectedListener {
                onTabSelected {
                    it?.run { vpContent?.currentItem = position }
                }
            }
        }
    }

    override fun initListener() {
        mWeChatVM.authorList.observe(this, Observer { authorList ->
            vpContent?.adapter = WeChatAuthorPageAdapter(authorList, childFragmentManager)
        })

        etSearch.addTextChangedListener {
            afterTextChanged { mWeChatVM.keyword.value = it?.toString() }
        }

        btnClear.setOnClickListener { etSearch.setText("") }
    }

    override fun initData() {
        mWeChatVM.getWeChatAuthors()
    }
}