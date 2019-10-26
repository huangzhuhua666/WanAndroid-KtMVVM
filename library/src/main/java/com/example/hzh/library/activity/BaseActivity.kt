package com.example.hzh.library.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.hzh.library.R
import com.example.hzh.library.extension.toast
import com.example.hzh.library.http.APIException
import com.example.hzh.library.viewmodel.BaseVM
import com.example.hzh.library.widget.StatusLayout
import com.example.hzh.library.widget.dialog.LoadingDialog
import com.gyf.immersionbar.ktx.immersionBar
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/09/09.
 */
abstract class BaseActivity<B : ViewDataBinding, VM : BaseVM> : AppCompatActivity() {

    companion object {

        private const val TAG = "Current Activity"
    }

    private val mLoadingDialog by lazy { LoadingDialog() }

    protected val mContext by lazy { this }

    protected var mBinding by Delegates.notNull<B>()
        private set

    protected abstract val mLayoutId: Int

    protected open val mTitleView: View?
        get() = null

    protected open val mStatusView: StatusLayout?
        get() = null

    protected open val mViewModel: VM?
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
        mBinding = DataBindingUtil.setContentView(this, mLayoutId)
        mBinding.lifecycleOwner = this

        if (isUseImmersionBar) immersionBar {
            mTitleView?.let { titleBar(it) }

            statusBarDarkFont(isStatusBarDarkFont, .2f)
        }

        Log.d(TAG, javaClass.simpleName)

        intent?.extras?.let { onGetBundle(it) }

        initView()
        initListener()

        mViewModel?.let {
            it.isShowLoading.observe(this, Observer { isShowLoading ->
                if (isShowLoading && !mLoadingDialog.isShowing()) mLoadingDialog.show(mContext)
                else if (!isShowLoading && mLoadingDialog.isShowing()) mLoadingDialog.dismiss()
            })

            it.toastTip.observe(this, Observer { tip -> toast(tip) })

            it.exception.observe(this, Observer { e ->
                if (e is APIException && e.isLoginExpired()) onLoginExpired(e)
                else onError(e)
            })
        }

        initData()
    }

    protected open fun onGetBundle(bundle: Bundle) {}

    protected abstract fun initView()

    protected abstract fun initListener()

    protected abstract fun initData()

    protected open fun onError(e: Throwable) {
        if (e is APIException) toast(e.msg)
        else toast(e.message ?: getString(R.string.unknown_error))
    }

    protected open fun onLoginExpired(e: APIException) {
        toast(e.msg)
    }
}