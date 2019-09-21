package com.example.hzh.ktmvvm.view.fragment

import androidx.lifecycle.Observer
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.adapter.WeChatAuthorPageAdapter
import com.example.hzh.ktmvvm.viewmodel.WeChatAuthorVM
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

    private val mWeChatVM by lazy { obtainVM(WeChatAuthorVM::class.java) }

    override fun initView() {
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
    }

    override fun initData() {
        mWeChatVM.getWeChatAuthors()
    }
}