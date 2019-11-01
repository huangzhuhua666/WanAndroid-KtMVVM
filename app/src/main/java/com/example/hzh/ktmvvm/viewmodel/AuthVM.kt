package com.example.hzh.ktmvvm.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.User
import com.example.hzh.ktmvvm.data.model.UserModel
import com.example.hzh.ktmvvm.util.OperateCallback
import com.example.hzh.library.viewmodel.BaseVM
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Create by hzh on 2019/10/21.
 */
class AuthVM : BaseVM() {

    private val userModel by lazy { UserModel() }

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val rePassword = MutableLiveData<String>()
    val user = MutableLiveData<User>()

    fun login() {
        doOnIO(
            tryBlock = {
                userModel.login(username.value, password.value, object : OperateCallback<User> {

                    override fun onInputIllegal(@StringRes tip: Int) {
                        toastTip.postValue(tip)
                    }

                    override fun onPreOperate() {
                        isShowLoading.postValue(true)
                    }

                    override fun onCallback(data: User) {
                        user.postValue(data)
                        LiveEventBus.get("auth").post(true)
                        toastTip.postValue(R.string.login_success)
                    }
                })
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { isShowLoading.value = false })
    }

    fun register() {
        doOnIO(
            tryBlock = {
                userModel.register(
                    username.value,
                    password.value,
                    rePassword.value,
                    object : OperateCallback<User> {

                        override fun onInputIllegal(@StringRes tip: Int) {
                            toastTip.postValue(tip)
                        }

                        override fun onPreOperate() {
                            isShowLoading.postValue(true)
                        }

                        override fun onCallback(data: User) {
                            user.postValue(data)
                            LiveEventBus.get("auth").post(true)
                            toastTip.postValue(R.string.register_success)
                        }
                    })
            },
            catchBlock = { e -> e.printStackTrace() },
            finallyBlock = { isShowLoading.value = false })
    }
}