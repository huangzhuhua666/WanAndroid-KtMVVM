package com.example.hzh.ktmvvm.app

import android.app.Application
import androidx.multidex.MultiDex

/**
 * Create by hzh on 2019/08/23.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}