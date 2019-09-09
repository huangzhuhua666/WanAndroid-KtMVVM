package com.example.hzh.ktmvvm

import com.example.hzh.ktmvvm.app.App
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.http.APIException
import kotlinx.coroutines.*

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun initData() {
        launch(Dispatchers.IO) {
            val test = App.httpClient.getService(Test::class.java)
            try {
                test.login("sdhzh666", "39547android666")
                delay(3000L)
                test.logout()
            } catch (e: APIException) {
                e.printStackTrace()
            }
        }
    }
}
