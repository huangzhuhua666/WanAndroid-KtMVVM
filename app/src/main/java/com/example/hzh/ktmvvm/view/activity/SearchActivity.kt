package com.example.hzh.ktmvvm.view.activity

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.example.hzh.ktmvvm.compose.search.SearchScreen
import com.example.hzh.ktmvvm.compose.util.LocalBackPressedDispatcher
import com.example.hzh.library.extension.startActivity
import com.google.accompanist.insets.ProvideWindowInsets

/**
 * Create by hzh on 2021/12/7
 */
class SearchActivity : AppCompatActivity() {

    companion object {

        fun open(activity: Activity) {
            activity.startActivity<SearchActivity>()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProvideWindowInsets(consumeWindowInsets = false) {
                CompositionLocalProvider(
                    LocalBackPressedDispatcher provides onBackPressedDispatcher
                ) {
                    SearchScreen(
                        onBackClick = { onBackPressed() },
                        onWebActivityOpen = { url, title ->
                            WebActivity.open(this, url, title)
                        },
                        onLoginAction = { AuthActivity.open(this) }
                    )
                }
            }
        }
    }

//    override fun onLoginExpired(e: APIException) {
//        super.onLoginExpired(e)
//        AuthActivity.open(mContext)
//    }
}