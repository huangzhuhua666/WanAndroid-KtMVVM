package com.example.hzh.ktmvvm.compose.web

import android.graphics.Bitmap
import android.net.http.SslError
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.compose.common.MarqueeText
import com.example.hzh.ktmvvm.compose.common.MyIconButton
import com.example.hzh.ktmvvm.compose.common.WebView
import com.example.hzh.ktmvvm.compose.common.rememberProgressState
import com.example.hzh.ktmvvm.util.InjectorUtils
import com.example.hzh.ktmvvm.viewmodel.WebVM
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Create by hzh on 2021/11/24
 */
data class WebTitleCallbacks(

    val onBackClick: () -> Unit,

    val onCloseClick: () -> Unit
)

@Composable
fun WebScreen(
    originTitle: String?,
    url: String?,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val webVM: WebVM = viewModel(
        factory = InjectorUtils.provideWebVMFactory(originTitle = originTitle ?: "")
    )
    val title by webVM.title.collectAsState()
    val progressState = rememberProgressState(max = 100)

    Surface {
        Column {
            WebTitle(
                title = title,
                callbacks = WebTitleCallbacks(
                    onBackClick = onBackClick,
                    onCloseClick = onCloseClick
                )
            )

            if (progressState.isShowProgressBar) {
                LinearProgressIndicator(
                    progress = progressState.ratio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = colorResource(id = R.color.color_ffe4b5),
                    backgroundColor = Color.White
                )
            }

            val loadingStr = stringResource(R.string.loading)
            WebView(
                url = url,
                modifier = Modifier.fillMaxSize(),
                onCloseClick = onCloseClick,
                overScrollMode = View.OVER_SCROLL_NEVER,
                webViewClient = object : WebViewClient() {

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        webVM.updateTitle(loadingStr)
                    }

                    override fun onReceivedSslError(
                        view: WebView?,
                        handler: SslErrorHandler?,
                        error: SslError?
                    ) {
                        handler?.proceed()
                    }
                },
                webChromeClient = object : WebChromeClient() {

                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        progressState.progress = newProgress
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        val newTitle = if (title?.trim().isNullOrEmpty()) originTitle else title
                        webVM.updateTitle(newTitle ?: "")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewWebScreen() {
    WebScreen(
        originTitle = "这是一个很长很长很长很长很长的标题 - WanAndroid",
        url = "https://www.baidu.com",
        onBackClick = { },
        onCloseClick = { }
    )
}

@Composable
private fun WebTitle(
    title: String,
    callbacks: WebTitleCallbacks
) {
    val titleColor = colorResource(R.color.appColor)
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(titleColor, false)
    Box(modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .background(titleColor)
        .height(49.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val btnModifier = Modifier.size(49.dp)

            MyIconButton(
                modifier = btnModifier,
                onClick = callbacks.onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White
                )
            }

            MyIconButton(
                modifier = btnModifier,
                onClick = callbacks.onCloseClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_white),
                    contentDescription = null,
                    tint = Color.White
                )
            }

            MarqueeText(
                text = title,
                modifier = Modifier.weight(1f),
                color = Color.White,
                fontSize = 18.sp,
                gradientEdgeColor = titleColor
            )
        }
    }
}