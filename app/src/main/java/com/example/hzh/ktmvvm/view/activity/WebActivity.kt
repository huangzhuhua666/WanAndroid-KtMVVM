package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import com.example.hzh.ktmvvm.compose.util.LocalBackPressedDispatcher
import com.example.hzh.ktmvvm.compose.web.WebScreen
import com.example.hzh.library.extension.startActivity
import com.google.accompanist.insets.ProvideWindowInsets

/**
 * Create by hzh on 2021/11/26
 */
class WebActivity : AppCompatActivity() {

    companion object {

        fun open(activity: Activity, url: String, title: String) =
            activity.startActivity<WebActivity>(bundleOf("url" to url, "title" to title))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                CompositionLocalProvider(
                    LocalBackPressedDispatcher provides onBackPressedDispatcher
                ) {
                    WebScreen(
                        originTitle = intent.extras?.getString("title", ""),
                        url = intent.extras?.getString("url", ""),
                        onBackClick = { onBackPressed() },
                        onCloseClick = { finish() }
                    )
                }
            }
        }
    }
}