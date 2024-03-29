package com.example.hzh.library.activity

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.observe
import com.example.hzh.library.R
import com.example.hzh.library.extension.hideKeyboard
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

    protected open val mViewModel: VM? = null

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

    protected open val isClickHideKeyboard: Boolean
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

        mViewModel?.let { vm ->
            vm.isShowLoading.observe(mContext) {
                if (it) mLoadingDialog.show(mContext)
                else if (!it && mLoadingDialog.isShowing()) mLoadingDialog.dismiss()
            }

            vm.toastTip.observe(mContext) { toast(it) }

            vm.exception.observe(mContext) {
                if (it is APIException && it.isLoginExpired()) onLoginExpired(it)
                else onError(it)
            }
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (isClickHideKeyboard && ev?.actionMasked == MotionEvent.ACTION_DOWN) {
            currentFocus?.let {
                // 判断是否要隐藏键盘
                if (isShouldHideKeyboard(it, ev)) hideKeyboard(it)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 焦点在EditText：点击区域在EditText中，不隐藏键盘
     * 焦点不在EditText：不隐藏键盘
     */
    private fun isShouldHideKeyboard(view: View, ev: MotionEvent): Boolean {
        return if (view is EditText) {
            val l = IntArray(2)
            view.getLocationInWindow(l)

            val left = l[0]
            val top = l[1]
            val right = left + view.width
            val bottom = top + view.height

            val x = ev.x.toInt()
            val y = ev.y.toInt()

            !(x in left..right && y in top..bottom)
        } else false
    }
}