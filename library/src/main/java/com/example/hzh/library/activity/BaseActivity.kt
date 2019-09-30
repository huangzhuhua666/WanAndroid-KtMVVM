package com.example.hzh.library.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ktx.immersionBar
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/09.
 */
abstract class BaseActivity<B: ViewDataBinding> : AppCompatActivity() {

    companion object {

        private const val TAG = "Current Activity"
    }

    protected val mContext by lazy { this }

    protected var mBinding by Delegates.notNull<B>()
    private set

    protected abstract val layoutId: Int

    protected open val titleView: View?
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
        mBinding = DataBindingUtil.setContentView(this, layoutId)
        mBinding.lifecycleOwner = this

        if (isUseImmersionBar) immersionBar {
            titleView?.let { titleBar(it) }

            statusBarDarkFont(isStatusBarDarkFont, .2f)
        }

        Log.d(TAG, javaClass.simpleName)

        intent?.extras?.let { onGetBundle(it) }

        initView()
        initListener()
        initData()
    }

    protected open fun onGetBundle(bundle: Bundle) {}

    protected abstract fun initView()

    protected abstract fun initListener()

    protected abstract fun initData()
}