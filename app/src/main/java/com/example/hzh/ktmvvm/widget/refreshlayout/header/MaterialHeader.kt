package com.example.hzh.ktmvvm.widget.refreshlayout.header

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hzh.ktmvvm.widget.refreshlayout.SwipeRefreshState
import com.example.hzh.ktmvvm.widget.refreshlayout.painter.CircularProgressPainter
import com.example.hzh.ktmvvm.widget.refreshlayout.util.rememberUpdatedSlingshot

/**
 * @author huangzhuhua
 * @date 2022/1/13
 */
private const val CROSS_FADE_DURATION_MS = 100

@Composable
fun MaterialHeader(state: SwipeRefreshState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        MaterialIndicator(state)
    }
}

/**
 * Indicator composable which is typically used in conjunction with [SwipeRefresh].
 *
 * @param state The [SwipeRefreshState] passed into the [SwipeRefresh] `indicator` block.
 * @param modifier The modifier to apply to this layout.
 * @param fade Whether the arrow should fade in/out as it is scrolled in. Defaults to true.
 * @param scale Whether the indicator should scale up/down as it is scrolled in. Defaults to false.
 * @param arrowEnabled Whether an arrow should be drawn on the indicator. Defaults to true.
 * @param backgroundColor The color of the indicator background surface.
 * @param contentColor The color for the indicator's contents.
 * @param shape The shape of the indicator background surface. Defaults to [CircleShape].
 * @param largeIndication Whether the indicator should be 'large' or not. Defaults to false.
 * @param elevation The size of the shadow below the indicator.
 */
@Composable
private fun MaterialIndicator(
    state: SwipeRefreshState,
    modifier: Modifier = Modifier,
    fade: Boolean = true,
    scale: Boolean = false,
    arrowEnabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
    refreshingOffset: Dp = 16.dp,
    largeIndication: Boolean = false,
    elevation: Dp = 6.dp,
) {
    val adjustedElevation = when {
        state.isRefreshing -> elevation
        state.indicatorOffset > 0.5f -> elevation
        else -> 0.dp
    }

    val sizes = if (largeIndication) LargeSizes else DefaultSizes
    val indicatorHeight = LocalDensity.current.run { sizes.size.roundToPx() }
    val refreshingOffsetPx = LocalDensity.current.run { refreshingOffset.toPx() }

    val slingshot = rememberUpdatedSlingshot(
        offsetY = state.indicatorOffset,
        maxOffsetY = state.minTrigger,
        height = indicatorHeight,
    )

    var offset by remember { mutableStateOf(0f) }

    // If the user is currently swiping, we use the 'slingshot' offset directly
    if (state.isSwipeInProgress) {
        offset = slingshot.offset.toFloat()
    }

    LaunchedEffect(state.isSwipeInProgress, state.isRefreshing) {
        // If there's no swipe currently in progress, animate to the correct resting position
        if (state.isSwipeInProgress) {
            return@LaunchedEffect
        }

        animate(
            initialValue = offset,
            targetValue = when {
                state.isRefreshing -> indicatorHeight + refreshingOffsetPx
                else -> 0f
            }
        ) { value, _ ->
            offset = value
        }
    }

    Surface(
        modifier = modifier
            .size(size = sizes.size)
            .graphicsLayer {
                // Translate the indicator according to the slingshot
                translationY = offset - indicatorHeight

                val scaleFraction = if (scale && !state.isRefreshing) {
                    val progress = offset / state.minTrigger.coerceAtLeast(1f)

                    // We use LinearOutSlowInEasing to speed up the scale in
                    LinearOutSlowInEasing
                        .transform(progress)
                        .coerceIn(0f, 1f)
                } else {
                    1f
                }

                scaleX = scaleFraction
                scaleY = scaleFraction
            },
        shape = shape,
        color = backgroundColor,
        elevation = adjustedElevation
    ) {
        val painter = remember { CircularProgressPainter() }.also {
            it.arcRadius = sizes.arcRadius
            it.strokeWidth = sizes.strokeWidth
            it.arrowWidth = sizes.arrowWidth
            it.arrowHeight = sizes.arrowHeight
            it.arrowEnabled = arrowEnabled && !state.isRefreshing
            it.color = contentColor
            it.alpha = if (fade) (state.indicatorOffset / state.minTrigger).coerceIn(0f, 1f) else 1f
            it.startTrim = slingshot.startTrim
            it.endTrim = slingshot.endTrim
            it.rotation = slingshot.rotation
            it.arrowScale = slingshot.arrowScale
        }

        // This shows either an Image with CircularProgressPainter or a MaterialRefreshIndicator,
        // depending on refresh state
        Crossfade(
            targetState = state.isRefreshing,
            animationSpec = tween(durationMillis = CROSS_FADE_DURATION_MS)
        ) { refreshing ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (refreshing) {
                    CircularProgressIndicator(
                        color = contentColor,
                        strokeWidth = sizes.strokeWidth,
                        modifier = Modifier.size((sizes.arcRadius + sizes.strokeWidth) * 2),
                    )
                } else {
                    Image(
                        painter = painter,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

/**
 * A class to encapsulate details of different indicator sizes.
 *
 * @param size The overall size of the indicator.
 * @param arcRadius The radius of the arc.
 * @param strokeWidth The width of the arc stroke.
 * @param arrowWidth The width of the arrow.
 * @param arrowHeight The height of the arrow.
 */
@Immutable
private data class SwipeRefreshIndicatorSizes(
    val size: Dp,
    val arcRadius: Dp,
    val strokeWidth: Dp,
    val arrowWidth: Dp,
    val arrowHeight: Dp,
)

/**
 * The default/normal size values for [MaterialHeader].
 */
private val DefaultSizes = SwipeRefreshIndicatorSizes(
    size = 40.dp,
    arcRadius = 7.5.dp,
    strokeWidth = 2.5.dp,
    arrowWidth = 10.dp,
    arrowHeight = 5.dp,
)

/**
 * The 'large' size values for [MaterialHeader].
 */
private val LargeSizes = SwipeRefreshIndicatorSizes(
    size = 56.dp,
    arcRadius = 11.dp,
    strokeWidth = 3.dp,
    arrowWidth = 12.dp,
    arrowHeight = 6.dp,
)