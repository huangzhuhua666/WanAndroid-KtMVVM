package com.example.hzh.ktmvvm.widget.refreshlayout.util

import androidx.compose.runtime.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * @author huangzhuhua
 * @date 2022/1/13
 */
private const val MAX_PROGRESS_ARC = 0.8f

/**
 * A utility function that calculates various aspects of 'slingshot' behavior.
 * Adapted from SwipeRefreshLayout#moveSpinner method.
 *
 * @param offsetY The current y offset.
 * @param maxOffsetY The max y offset.
 * @param height The height of the item to slingshot.
 */
@Composable
internal fun rememberUpdatedSlingshot(
    offsetY: Float,
    maxOffsetY: Float,
    height: Int
): Slingshot {
    val offsetPercent = min(1f, offsetY / maxOffsetY)
    val adjustedPercent = max(offsetPercent - 0.4f, 0f) * 5 / 3
    val extraOffset = abs(offsetY) - maxOffsetY

    // Can accommodate custom start and slingshot distance here
    val tensionSlingshotPercent = max(0f, min(extraOffset, maxOffsetY * 2) / maxOffsetY)
    val tensionPercent = ((tensionSlingshotPercent / 4) - (tensionSlingshotPercent / 4).pow(2)) * 2
    val extraMove = maxOffsetY * tensionPercent * 2
    val targetY = height + ((maxOffsetY * offsetPercent) + extraMove).toInt()
    val offset = targetY - height
    val strokeStart = adjustedPercent * 0.8f

    val startTrim = 0f
    val endTrim = strokeStart.coerceAtMost(MAX_PROGRESS_ARC)

    val rotation = (-0.25f + 0.4f * adjustedPercent + tensionPercent * 2) * 0.5f
    val arrowScale = min(1f, adjustedPercent)

    return remember { Slingshot() }.also {
        it.offset = offset
        it.startTrim = startTrim
        it.endTrim = endTrim
        it.rotation = rotation
        it.arrowScale = arrowScale
    }
}

@Stable
internal class Slingshot {

    var offset by mutableStateOf(0)
    var startTrim by mutableStateOf(0f)
    var endTrim by mutableStateOf(0f)
    var rotation by mutableStateOf(0f)
    var arrowScale by mutableStateOf(0f)
}