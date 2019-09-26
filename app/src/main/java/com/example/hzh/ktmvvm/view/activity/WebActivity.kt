package com.example.hzh.ktmvvm.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_web.*

/**
 * Create by hzh on 2019/09/16.
 */
class WebActivity : BaseActivity() {

    companion object {

        fun open(ctx: Context, url: String, title: String) {
            ctx.startActivity(Intent(ctx, WebActivity::class.java).apply {
                putExtras(bundleOf("url" to url, "title" to title))
            })
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_web

    override val titleView: View?
        get() = llTitle

    private var url by DelegateExt.notNullSingleValue<String>()

    private var title by DelegateExt.notNullSingleValue<String>()

    private val binding by lazy { mBinding as ActivityWebBinding }

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
                    binding.run {
                        isLoading = true
                        title = getString(R.string.loading)
                    }
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    binding.run {
                        isLoading = false
                        title = if (view.title != "") view.title else this@WebActivity.title
                    }
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
                    binding.progress = newProgress
                }
            }
        }
    }

    override fun initData() {
        binding.url = url
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {
            web.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}