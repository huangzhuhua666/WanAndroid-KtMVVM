package com.example.hzh.ktmvvm.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.User
import com.example.hzh.ktmvvm.data.model.UserModel
import com.example.hzh.ktmvvm.util.Event
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

    /**
     * 登录
     */
    fun login() {
        doOnIO(
            tryBlock = {
                userModel.login(username.value, password.value, object : OperateCallback<User> {

                    override fun onInputIllegal(@StringRes tip: Int) = _toastTip.postValue(tip)

                    override fun onPreOperate() = _isShowLoading.postValue(true)

                    override fun onCallback(data: User) {
                        LiveEventBus.get(Event.AUTH).post(true)
                        _toastTip.postValue(R.string.login_success)
                    }
                })
            },
            finallyBlock = { _isShowLoading.value = false })
    }

    /**
     * 注册
     */
    fun register() {
        doOnIO(
            tryBlock = {
                userModel.register(
                    username.value,
                    password.value,
                    rePassword.value,
                    object : OperateCallback<User> {

                        override fun onInputIllegal(@StringRes tip: Int) = _toastTip.postValue(tip)

                        override fun onPreOperate() = _isShowLoading.postValue(true)

                        override fun onCallback(data: User) {
                            LiveEventBus.get(Event.AUTH).post(true)
                            _toastTip.postValue(R.string.register_success)
                        }
                    })
            },
            finallyBlock = { _isShowLoading.value = false })
    }

    /**
     * 退出登录
     */
    fun logout() {
        _isShowLoading.value = true
        doOnIO(
            tryBlock = {
                userModel.logout().also {
                    LiveEventBus.get(Event.AUTH).post(false)
                    _toastTip.postValue(R.string.logout_success)
                }
            },
            finallyBlock = { _isShowLoading.value = false }
        )
    }
}