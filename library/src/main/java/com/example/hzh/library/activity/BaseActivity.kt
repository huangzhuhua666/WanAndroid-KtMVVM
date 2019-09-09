package com.example.hzh.library.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * Create by hzh on 2019/09/09.
 */
abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    companion object {

        private const val TAG = "Current Activity"
    }

    protected val context by lazy { this }

    protected abstract val layoutId: Int

    protected open val title: View?
        get() = null

    /**
     * 使用沉浸式状态栏
     */
    protected open val isUseImmersionBar: Boolean
        get() = true

    /**
     * 状态栏字体黑色
     */
    protected open val isStatusBarDarkFont: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        if (isUseImmersionBar) immersionBar {
            title?.let { titleBar(it) }

            statusBarDarkFont(isStatusBarDarkFont, .2f)
        }

        Log.d(TAG, javaClass.name)

        intent?.extras?.let { onGetBundle(it) }

        initView()
        initListener()
        initData()
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }

    protected open fun onGetBundle(bundle: Bundle) {}

    protected abstract fun initView()

    protected abstract fun initListener()

    protected abstract fun initData()
}