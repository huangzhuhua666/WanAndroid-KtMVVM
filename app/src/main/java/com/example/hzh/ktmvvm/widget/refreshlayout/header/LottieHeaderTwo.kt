package com.example.hzh.ktmvvm.widget.refreshlayout.header

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.widget.refreshlayout.SwipeRefreshState

@Composable
fun LottieHeaderTwo(state: SwipeRefreshState) {
    var isPlaying by remember { mutableStateOf(false) }
    val speed by remember { mutableStateOf(1f) }

    isPlaying = state.isRefreshing

    val lottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.refresh_two))
    val progress by animateLottieCompositionAsState(
        // 动画资源句柄
        composition = lottieComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = progress,
            modifier = Modifier
                .padding(vertical = 5.dp)
                .size(150.dp)
        )
    }
}