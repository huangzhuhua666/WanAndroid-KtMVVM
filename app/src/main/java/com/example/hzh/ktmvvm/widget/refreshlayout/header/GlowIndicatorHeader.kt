package com.example.hzh.ktmvvm.widget.refreshlayout.header

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hzh.ktmvvm.widget.refreshlayout.SwipeRefreshState

@Composable
fun GlowIndicatorHeader(
    state: SwipeRefreshState,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        modifier = Modifier
            .drawWithCache {
                onDrawBehind {
                    val distance = state.minTrigger
                    val progress = (state.indicatorOffset / distance).coerceIn(0f, 1f)
                    // We draw a translucent glow
                    val brush = Brush.verticalGradient(
                        0f to color.copy(alpha = 0.45f),
                        1f to color.copy(alpha = 0f)
                    )
                    // And fade the glow in/out based on the swipe progress
                    if (state.isSwipeInProgress) {
                        drawRect(
                            brush = brush,
                            alpha = FastOutSlowInEasing.transform(progress)
                        )
                    }
                }
            }
            .fillMaxWidth()
            .height(72.dp)
    ) {
        if (state.isRefreshing) {
            // If we're refreshing, show an indeterminate progress indicator
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            // Otherwise we display a determinate progress indicator with the current swipe progress
            val trigger = state.minTrigger
            val progress = (state.indicatorOffset / trigger).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}