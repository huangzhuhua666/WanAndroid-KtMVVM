package com.example.hzh.ktmvvm.view.fragment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.FragmentLoginBinding
import com.example.hzh.ktmvvm.viewmodel.AuthVM
import com.example.hzh.library.extension.obtainVM
import com.example.hzh.library.fragment.BaseFragment
import com.jeremyliao.liveeventbus.LiveEventBus

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
        mBinding.run {
            mViewModel?.let {
                btnCleanUsername.setOnClickListener { _ -> it.username.value = "" }

                btnCleanPassword.setOnClickListener { _ -> it.password.value = "" }
            }

            LiveEventBus.get("auth", Boolean::class.java)
                .observe(viewLifecycleOwner, Observer { isLogin ->
                    if (isLogin) {
                        mContext.setResult(Activity.RESULT_OK)
                        mContext.finish()
                    }
                })

            btnNoAccount.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.actionLoginToRegister)
            }
        }
    }
}