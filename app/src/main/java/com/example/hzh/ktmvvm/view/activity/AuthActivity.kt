package com.example.hzh.ktmvvm.view.activity

import android.content.Context
import android.content.Intent
import com.example.hzh.ktmvvm.R
import com.example.hzh.library.activity.BaseActivity

/**
 * Create by hzh on 2019/9/26.
 */
class AuthActivity : BaseActivity() {

    companion object {

        fun open(ctx: Context) {
            ctx.startActivity(Intent(ctx, AuthActivity::class.java))
        }
    }

    override val layoutId: Int
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