package com.example.hzh.ktmvvm.widget.refreshlayout.util

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hzh.ktmvvm.widget.refreshlayout.painter.ProgressPainter

/**
 * @author huangzhuhua
 * @date 2022/1/24
 */
@Composable
fun LoadingView() {
    val infiniteTransition = rememberInfiniteTransition()
    val degree by infiniteTransition.animateFloat(
        initialValue = 30f,
        targetValue = 3600f,
        animationSpec = infiniteRepeatable(
            animation = keyframes { durationMillis = 10000 },
            repeatMode = RepeatMode.Restart
        )
    )

    val progressPainter = remember { ProgressPainter() }.apply {
        progressDegree = (30 * (degree.toInt() / 30)).toFloat()
    }
    Image(
        painter = progressPainter,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
    )
}