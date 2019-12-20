package com.example.hzh.library.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.hzh.library.R
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.toast
import com.example.hzh.library.http.APIException
import com.example.hzh.library.viewmodel.BaseVM
import com.example.hzh.library.widget.StatusLayout
import com.example.hzh.library.widget.dialog.LoadingDialog
import com.gyf.immersionbar.ktx.immersionBar
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/10.
 */
abstract class BaseFragment<B : ViewDataBinding, VM : BaseVM> : Fragment() {

    companion object {

        private const val TAG = "Current Fragment"
    }

    private val mLoadingDialog by lazy { LoadingDialog() }

    private var isFirstIn = true

    protected val mContext by lazy { requireActivity() as BaseActivity<*, *> }

    protected var mBinding by Delegates.notNull<B>()
        private set

    protected abstract val mLayoutId: Int

    protected open val mTitleView: View?
        get() = null

    protected open val mStatusView: StatusLayout?
        get() = null

    protected open val mViewModel: VM? = null

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
            mTitleView?.let { titleBar(it) }

            statusBarDarkFont(isStatusBarDarkFont, .2f)
        }

        mViewModel?.let {
            it.isShowLoading.observe(this, Observer { isShowLoading ->
                if (isShowLoading) mLoadingDialog.show(mContext)
                else if (!isShowLoading && mLoadingDialog.isShowing()) mLoadingDialog.dismiss()
            })

            it.toastTip.observe(this, Observer { tip -> mContext.toast(tip) })

            it.exception.observe(this, Observer { e ->
                if (e is APIException && e.isLoginExpired()) onLoginExpired(e)
                else onError(e)
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isFirstIn = true
        return setContentView(inflater, container, mLayoutId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, javaClass.simpleName)

        arguments?.let { onGetBundle(it) }

        if (isFirstIn) {
            initData()
            isFirstIn = false
        }
    }

    protected open fun setContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        layoutId: Int
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mBinding.let {
            it.lifecycleOwner = viewLifecycleOwner
            it.root.parent?.let { p -> (p as ViewGroup).removeView(it.root) }
            it.root
        }
    }

    protected open fun onGetBundle(bundle: Bundle) {}

    protected abstract fun initView()

    protected abstract fun initListener()

    protected open fun initData() {}

    protected open fun onError(e: Throwable) {
        if (e is APIException) mContext.toast(e.msg)
        else mContext.toast(e.message ?: getString(R.string.unknown_error))
    }

    protected open fun onLoginExpired(e: APIException) {
        mContext.toast(e.msg)
    }
}