package com.example.hzh.ktmvvm.compose.common

import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.compose.util.BackPressHandler
import com.example.hzh.library.widget.ObsWebView

/**
 * Create by hzh on 2021/11/25
 */
@Composable
fun WebView(
    url: String?,
    modifier: Modifier = Modifier,
    onCloseClick: (() -> Unit)? = null,
    overScrollMode: Int = View.OVER_SCROLL_ALWAYS,
    webViewClient: WebViewClient? = null,
    webChromeClient: WebChromeClient? = null
) {
    val webView = rememberWebViewWithLifecycle()
    AndroidView(
        factory = {
            webView.apply {
                this.overScrollMode = overScrollMode
                webViewClient?.let {
                    this.webViewClient = it
                }
                webChromeClient?.let {
                    this.webChromeClient = it
                }
            }
        },
        modifier = modifier,
        update = { web ->
            url?.let {
                if (it == "") return@AndroidView
                if (!it.startsWith("http")) web.loadUrl("https://$it")
                else web.loadUrl(url)
            }
        }
    )

    if (onCloseClick != null) {
        BackPressHandler {
            if (webView.canGoBack()) webView.goBack()
            else onCloseClick()
        }
    }
}

@Composable
fun rememberWebViewWithLifecycle(): WebView {
    val context = LocalContext.current
    val webView = remember {
        ObsWebView(context).apply {
            id = R.id.webView
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(
        key1 = lifecycle,
        key2 = webView
    ) {
        lifecycle.addObserver(webView)
        onDispose {
            lifecycle.removeObserver(webView)
        }
    }

    return webView
}