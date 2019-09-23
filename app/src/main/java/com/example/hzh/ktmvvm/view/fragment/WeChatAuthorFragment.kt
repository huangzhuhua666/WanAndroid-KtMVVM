package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.WeChatAuthorPageAdapter
import com.example.hzh.ktmvvm.databinding.FragmentWechatAuthorBinding
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
import com.example.hzh.library.extension.addTextChangedListener
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_wechat_author.*

/**
 * Create by hzh on 2019/09/10.
 */
class WeChatAuthorFragment : BaseFragment() {

    companion object {

        fun newInstance(): WeChatAuthorFragment = WeChatAuthorFragment()
    }

    override val layoutId: Int
        get() = R.layout.fragment_wechat_author

    private val mWeChatVM by lazy { mContext.obtainVM(WeChatAuthorVM::class.java) }

    override fun initView() {
        (mBinding as FragmentWechatAuthorBinding).wechatVM = mWeChatVM

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