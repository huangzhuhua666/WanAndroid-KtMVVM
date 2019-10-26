package com.example.hzh.ktmvvm.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.os.bundleOf
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.databinding.ActivityWebBinding
import com.example.hzh.library.activity.BaseActivity
import com.example.hzh.library.extension.DelegateExt
import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Create by hzh on 2019/09/16.
 */
class WebActivity : BaseActivity<ActivityWebBinding, BaseVM>() {

    companion object {

        fun open(ctx: Context, url: String, title: String) {
            ctx.startActivity(Intent(ctx, WebActivity::class.java).apply {
                putExtras(bundleOf("url" to url, "title" to title))
            })
        }
    }

    override val mLayoutId: Int
        get() = R.layout.activity_web

    override val mTitleView: View?
        get() = llTitle

    private var url by DelegateExt.notNullSingleValue<String>()

    private var title by DelegateExt.notNullSingleValue<String>()

    override fun onGetBundle(bundle: Bundle) {
        bundle.let {
            url = it.getString("url", "")
            title = it.getString("title", "")
        }
    }

    override fun initView() {
        tvTitle?.isSelected = true

        lifecycle.addObserver(web)
    }

    override fun initListener() {
        btnBack.setOnClickListener { if (web.canGoBack()) web.goBack() else finish() }

        btnClose.setOnClickListener { finish() }

        web?.let {
            it.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    mBinding.run {
                        isLoading = true
                        title = getString(R.string.loading)
                    }
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    mBinding.isLoading = false
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    handler?.proceed()
                }
            }

            it.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    mBinding.progress = newProgress
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    mBinding.title = if (TextUtils.isEmpty(title)) this@WebActivity.title else title
                }
            }
        }
    }

    override fun initData() {
        mBinding.url = url
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}