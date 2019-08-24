package com.example.hzh.ktmvvm.app

import android.app.Application
import androidx.multidex.MultiDex
import com.example.hzh.library.extension.DelegateExt

/**
 * Create by hzh on 2019/08/23.
 */
class App : Application() {

    companion object {
        var context by DelegateExt.notNullSingleValue<App>()
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        MultiDex.install(this)
    }
}