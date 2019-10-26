package com.example.hzh.ktmvvm.view.fragment

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.FragmentLoginBinding
import com.example.hzh.ktmvvm.viewmodel.AuthVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Create by hzh on 2019/9/26.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding, AuthVM>() {

    override val mLayoutId: Int
        get() = R.layout.fragment_login

    override val mViewModel: AuthVM?
        get() = obtainVM(AuthVM::class.java)

    override fun initView() {
        mBinding.authVM = mViewModel
    }

    override fun initListener() {
        mViewModel?.let {
            btnCleanUsername.setOnClickListener { _ -> it.username.value = "" }

            btnCleanPassword.setOnClickListener { _ -> it.password.value = "" }

            it.user.observe(this, Observer { user ->
                mContext.setResult(Activity.RESULT_OK, Intent().putExtra("user", user))
                mContext.finish()
            })
        }

        btnNoAccount.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.actionLoginToRegister)
        }
    }

    override fun initData() {

    }
}