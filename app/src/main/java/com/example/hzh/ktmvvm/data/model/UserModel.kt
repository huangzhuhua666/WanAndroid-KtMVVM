package com.example.hzh.ktmvvm.data.model

import android.text.TextUtils
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.User
import com.example.hzh.ktmvvm.data.repository.UserRepository
import com.example.hzh.ktmvvm.util.OperateCallback

/**
 * Create by hzh on 2019/10/21.
 */
class UserModel {

    private val repo by lazy { UserRepository.getInstance() }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @param callback 操作回调
     */
    suspend fun login(username: String?, password: String?, callback: OperateCallback<User>) {
        if (TextUtils.isEmpty(username)) {
            callback.onInputIllegal(R.string.please_input_username)
            return
        }

        if (TextUtils.isEmpty(password)) {
            callback.onInputIllegal(R.string.please_input_password)
            return
        }

        callback.onPreOperate()

        repo.login(username!!, password!!).let { callback.onCallback(it) }
    }

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param rePassword 重复密码
     * @param callback 操作回调
     */
    suspend fun register(
        username: String?,
        password: String?,
        rePassword: String?,
        callback: OperateCallback<User>
    ) {
        if (TextUtils.isEmpty(username)) {
            callback.onInputIllegal(R.string.please_input_username)
            return
        }

        if (TextUtils.isEmpty(password)) {
            callback.onInputIllegal(R.string.please_input_password)
            return
        }

        if (TextUtils.isEmpty(rePassword)) {
            callback.onInputIllegal(R.string.please_input_repassword)
            return
        }

        if (password != rePassword) {
            callback.onInputIllegal(R.string.twice_input_password_not_equal)
            return
        }

        callback.onPreOperate()

        repo.register(username!!, password!!, rePassword!!).let { callback.onCallback(it) }
    }

    /**
     * 退出登录
     */
    suspend fun logout() = repo.logout()
}