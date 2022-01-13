package com.example.hzh.ktmvvm.widget.refreshlayout

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.runtime.*
import kotlin.math.abs
import kotlin.math.pow

/**
 * @author huangzhuhua
 * @date 2021/12/30
 */
@Composable
internal fun rememberSwipeRefreshState(
    isRefreshing: Boolean,
    minTrigger: Float,
    maxDrag: Float
): SwipeRefreshState = remember {
    SwipeRefreshState(isRefreshing, minTrigger, maxDrag)
}.also {
    it.isRefreshing = isRefreshing
    it.minTrigger = minTrigger
    it.maxDrag = maxDrag
}

@Stable
class SwipeRefreshState(
    isRefreshing: Boolean,
    minTrigger: Float,
    maxDrag: Float
) {

    /**
     * Whether this [SwipeRefreshState] is currently refreshing or not.
     */
    var isRefreshing by mutableStateOf(isRefreshing)
        internal set

    /**
     * Whether a swipe/drag is currently in progress.
     */
    var isSwipeInProgress by mutableStateOf(false)
        internal set

    private val mIndicatorOffset = Animatable(0f)

    /**
     * The current offset for the indicator, in pixels.
     */
    val indicatorOffset: Float
        get() = mIndicatorOffset.value

    /**
     * 刷新生效距离
     */
    var minTrigger by mutableStateOf(minTrigger)
        internal set

    /**
     * 最大下拉距离
     */
    var maxDrag by mutableStateOf(maxDrag)
        internal set

    var headerState by mutableStateOf(RefreshHeaderState.PullDownToRefresh)
        internal set

    private val mMutatorMutex by lazy { MutatorMutex() }

    internal suspend fun animateOffsetTo(offset: Float) {
        mMutatorMutex.mutate {
            mIndicatorOffset.animateTo(offset)
            updateHeaderState()
        }
    }

    private fun updateHeaderState() {
        headerState = when {
            isRefreshing -> RefreshHeaderState.Refreshing
            isSwipeInProgress -> {
                if (indicatorOffset > minTrigger) {
                    RefreshHeaderState.ReleaseToRefresh
                } else {
                    RefreshHeaderState.PullDownToRefresh
                }
            }
            else -> RefreshHeaderState.PullDownToRefresh
        }
    }

    /**
     * Dispatch scroll delta in pixels from touch events.
     */
    internal suspend fun dispatchScrollDelta(delta: Float) {
        mMutatorMutex.mutate(MutatePriority.UserInput) {
            val slingShotOffset = getSlingShotOffset(mIndicatorOffset.value + delta, maxDrag)
            mIndicatorOffset.snapTo(slingShotOffset)
            updateHeaderState()
        }
    }

    private fun getSlingShotOffset(offsetY: Float, maxOffsetY: Float): Float {
        val offsetPercent = 1f.coerceAtMost(offsetY / maxOffsetY)
        val extraOffset = abs(offsetY) - maxOffsetY

        val tensionSlingshotPercent =
            0f.coerceAtLeast(extraOffset.coerceAtMost(maxOffsetY * 2) / maxOffsetY)
        val tensionPercent =
            ((tensionSlingshotPercent / 4) - (tensionSlingshotPercent / 4).pow(2)) * 2
        val extraMove = maxOffsetY * tensionPercent * 2
        return maxOffsetY * offsetPercent + extraMove
    }
}

enum class RefreshHeaderState {

    PullDownToRefresh,

    Refreshing,

    ReleaseToRefresh
}