package com.example.hzh.ktmvvm.view.activity

import android.content.Context
import android.content.Intent
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.ActivityAuthBinding
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/9/26.
 */
class AuthActivity : BaseActivity<ActivityAuthBinding, BaseVM>() {

    companion object {

        fun open(ctx: Context) {
            ctx.startActivity(Intent(ctx, AuthActivity::class.java))
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_auth

    override val isStatusBarDarkFont: Boolean
        get() = true

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {

    }
}