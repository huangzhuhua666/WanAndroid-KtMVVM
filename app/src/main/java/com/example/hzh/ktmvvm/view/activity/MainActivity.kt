package com.example.hzh.ktmvvm.view.activity

import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.Test
import com.example.hzh.ktmvvm.adapter.PageAdapter
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.view.fragment.HomeFragment
import com.example.hzh.ktmvvm.view.fragment.ProjectFragment
import com.example.hzh.ktmvvm.view.fragment.PublicAuthorFragment
import com.example.hzh.ktmvvm.view.fragment.SystemFragment
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.http.APIException
import com.gyf.immersionbar.ktx.immersionBar
import com.gyf.immersionbar.ktx.setFitsSystemWindows
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override val isStatusBarDarkFont: Boolean
        get() = true

    override fun initView() {
        immersionBar { setFitsSystemWindows() }

        val list = listOf(
            HomeFragment.newInstance(), SystemFragment.newInstance(),
            PublicAuthorFragment.newInstance(), ProjectFragment.newInstance()
        )
        val adapter = PageAdapter(list, supportFragmentManager)
        vpContent.adapter = adapter
        indicator.setViewPager(vpContent)
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
