package com.example.hzh.ktmvvm.base

import androidx.core.content.edit
import androidx.databinding.ViewDataBinding
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.util.Event
import com.example.hzh.library.fragment.BaseFragment
import com.example.hzh.library.http.APIException
import com.example.hzh.library.viewmodel.BaseVM
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Create by hzh on 2019/11/6.
 */
abstract class WanFragment<B : ViewDataBinding, VM : BaseVM> : BaseFragment<B, VM>() {

    override fun onLoginExpired(e: APIException) {
        super.onLoginExpired(e)
        App.isLogin = false
        App.configSP.edit { clear() }
        LiveEventBus.get(Event.AUTH).post(false)
    }
}