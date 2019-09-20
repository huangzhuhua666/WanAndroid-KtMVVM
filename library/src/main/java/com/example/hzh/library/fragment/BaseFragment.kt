package com.example.hzh.library.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.hzh.library.activity.BaseActivity
import com.gyf.immersionbar.ktx.immersionBar
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/10.
 */
abstract class BaseFragment : Fragment() {

    companion object {

        private const val TAG = "Current Fragment"
    }

    protected val mContext by lazy { activity as BaseActivity }

    private var mRootView: View? = null

    protected var mBinding by Delegates.notNull<ViewDataBinding>()
        private set

    private var isFirstIn = true

    protected abstract val layoutId: Int

    protected open val titleView: View?
        get() = null

    /**
     * 使用沉浸式状态栏
     */
    protected open val isUseImmersionBar: Boolean
        get() = false

    /**
     * 状态栏字体黑色
     */
    protected open val isStatusBarDarkFont: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isUseImmersionBar) immersionBar {
            titleView?.let { titleBar(it) }

            statusBarDarkFont(isStatusBarDarkFont, .2f)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isFirstIn = true
        return setContentView(inflater, container, layoutId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let { onGetBundle(it) }

        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, javaClass.simpleName)

        if (isFirstIn) {
            initData()
            isFirstIn = false
        }

        lazyLoad()
    }

    protected open fun setContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        layoutId: Int
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding.let {
            it.lifecycleOwner = this
            mRootView = mRootView ?: it.root
            mRootView
        }
    }

    protected open fun onGetBundle(bundle: Bundle) {}

    protected abstract fun initView()

    protected abstract fun initListener()

    protected open fun initData() {}

    protected open fun lazyLoad() {}
}