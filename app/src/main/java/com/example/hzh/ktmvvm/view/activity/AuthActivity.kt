package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.content.Intent
import androidx.navigation.findNavController
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.ActivityAuthBinding
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.viewmodel.BaseVM

/**
 * Create by hzh on 2019/9/26.
 */
class AuthActivity : BaseActivity<ActivityAuthBinding, BaseVM>() {

    companion object {

        fun open(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, AuthActivity::class.java), requestCode)
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

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.navAuthFragment).navigateUp()
}