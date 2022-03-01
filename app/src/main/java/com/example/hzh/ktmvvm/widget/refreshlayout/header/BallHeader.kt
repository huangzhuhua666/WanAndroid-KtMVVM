package com.example.hzh.ktmvvm.widget.refreshlayout.header

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.widget.refreshlayout.SwipeRefreshState
import kotlinx.coroutines.delay

/**
 * @author huangzhuhua
 * @date 2022/1/13
 */
private const val BALL_NUM = 3

@Composable
fun BallHeader(state: SwipeRefreshState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        val radius = LocalDensity.current.run { 8.dp.toPx() }
        val circleSpacing = LocalDensity.current.run { 8.dp.toPx() }

        val interpolator = AccelerateDecelerateInterpolator()

        val ballColor = if (state.isRefreshing) {
            colorResource(id = R.color.color_33aaff)
        } else {
            colorResource(id = R.color.color_eeeeee)
        }

        val startTime = if (state.isRefreshing) System.currentTimeMillis() else 0
        var now by remember { mutableStateOf(0L) }
        LaunchedEffect(key1 = state.isRefreshing) {
            if (state.isRefreshing) {
                while (true) {
                    now = System.currentTimeMillis()
                    delay(60)
                }
            }
        }

        var scale by remember { mutableStateOf(1f) }

        Canvas(modifier = Modifier.wrapContentSize()) {
            for (i in 0 until BALL_NUM) {
                val time = now - startTime - 120 * (i + 1)
                var percent = if (time > 0) time % 750 / 750f else 0f
                percent = interpolator.getInterpolation(percent)

                scale = if (state.isRefreshing) {
                    if (percent < 0.5f) {
                        1 - percent * 2 * 0.7f
                    } else {
                        percent * 2 * 0.7f - 0.4f
                    }
                } else {
                    1f
                }

                drawCircle(
                    scale = scale,
                    index = i,
                    radius = radius,
                    circleSpacing = circleSpacing,
                    ballColor = ballColor
                )
            }
        }
    }
}

private fun DrawScope.drawCircle(
    scale: Float,
    index: Int,
    radius: Float,
    circleSpacing: Float,
    ballColor: Color,
) {
    scale(scale = scale, pivot = Offset(x = (index - 1) * (circleSpacing + radius * 2), y = 0f)) {
        drawCircle(
            color = ballColor,
            radius = radius,
            center = Offset(x = (index - 1) * (circleSpacing + radius * 2), y = 0f),
            alpha = 1f
        )
    }
}