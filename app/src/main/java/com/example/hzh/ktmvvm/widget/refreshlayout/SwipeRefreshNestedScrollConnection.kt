package com.example.hzh.ktmvvm.widget.refreshlayout

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * @author huangzhuhua
 * @date 2021/12/30
 */
internal class SwipeRefreshNestedScrollConnection(
    private val state: SwipeRefreshState,
    private val coroutineScope: CoroutineScope,
    private val onRefresh: () -> Unit,
) : NestedScrollConnection {

    var isRefreshEnabled = false
    var minTrigger = 0f

    private val mDragMultiplier = 0.5f

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset = when {
        !isRefreshEnabled -> Offset.Zero
        state.isRefreshing -> Offset.Zero
        // 用户正在上滑，拦截处理
        source == NestedScrollSource.Drag && available.y < 0 -> onScroll(available)
        else -> Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        !isRefreshEnabled -> Offset.Zero
        state.isRefreshing -> Offset.Zero
        // 用户正在下拉，处理剩余的偏移量
        source == NestedScrollSource.Drag && available.y > 0 -> onScroll(available)
        else -> Offset.Zero
    }

    private fun onScroll(available: Offset): Offset {
        state.isSwipeInProgress = true

        val newOffset = (available.y * mDragMultiplier + state.indicatorOffset).coerceAtLeast(0f)
        val dragConsumed = newOffset - state.indicatorOffset

        return if (dragConsumed.absoluteValue >= 0.5f) {
            coroutineScope.launch {
                state.dispatchScrollDelta(dragConsumed)
            }

            Offset(x = 0f, y = dragConsumed / mDragMultiplier)
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (!state.isRefreshing && state.indicatorOffset >= minTrigger) {
            onRefresh.invoke()
        }

        state.isSwipeInProgress = false

        // 不消费速度，直接返回 0
        return Velocity.Zero
    }
}