package com.example.hzh.ktmvvm.widget.refreshlayout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.example.hzh.ktmvvm.widget.refreshlayout.header.ClassicHeader

/**
 * @author huangzhuhua
 * @date 2021/12/30
 */
@Composable
fun SwipeRefreshLayout(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    isRefreshEnabled: Boolean = true,
    minTriggerRate: Float = 1f, // 生效高度与 indicator 高度的比例
    maxDragRate: Float = 2.5f, // 最大生效距离与 indicator 高度的比例
    header: @Composable (SwipeRefreshState) -> Unit = {
        ClassicHeader(it)
    },
    headerSwipeStyle: SwipeRefreshStyle = SwipeRefreshStyle.Translate,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val updateRefresh by rememberUpdatedState(onRefresh)

    var indicatorHeight by remember { mutableStateOf(1) }

    val minTrigger = indicatorHeight * minTriggerRate
    val maxDrag = indicatorHeight * maxDragRate

    val state = rememberSwipeRefreshState(
        isRefreshing = isRefreshing,
        minTrigger = minTrigger,
        maxDrag = maxDrag
    )
    LaunchedEffect(state.isSwipeInProgress, state.isRefreshing) {
        if (state.isSwipeInProgress) {
            return@LaunchedEffect
        }

        if (state.isRefreshing) {
            state.animateOffsetTo(minTrigger)
        } else {
            state.animateOffsetTo(0f)
        }
    }

    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeRefreshNestedScrollConnection(
            state = state,
            coroutineScope = coroutineScope,
            onRefresh = updateRefresh
        )
    }.also {
        it.isRefreshEnabled = isRefreshEnabled
        it.minTrigger = minTrigger
    }

    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    indicatorHeight = it.size.height
                }
                .let {
                    if (isHeaderNeedClip(state, indicatorHeight)) it.clipToBounds() else it
                }
                .offset {
                    getHeaderOffset(headerSwipeStyle, state, indicatorHeight)
                }
                .zIndex(getHeaderZIndex(headerSwipeStyle))
        ) {
            header(state)
        }

        Box(
            modifier = Modifier.offset {
                getContentOffset(headerSwipeStyle, state)
            }
        ) {
            content()
        }
    }
}

private fun isHeaderNeedClip(state: SwipeRefreshState, indicatorHeight: Int): Boolean {
    return state.indicatorOffset < indicatorHeight
}

private fun getHeaderOffset(
    style: SwipeRefreshStyle,
    state: SwipeRefreshState,
    indicatorHeight: Int
): IntOffset = when (style) {
    SwipeRefreshStyle.FixedBehind, SwipeRefreshStyle.FixedFront -> IntOffset(0, 0)
    else -> IntOffset(0, state.indicatorOffset.toInt() - indicatorHeight)
}

private fun getHeaderZIndex(style: SwipeRefreshStyle): Float =
    if (style == SwipeRefreshStyle.FixedFront || style == SwipeRefreshStyle.FixedContent) {
        1f
    } else {
        0f
    }

private fun getContentOffset(
    style: SwipeRefreshStyle,
    state: SwipeRefreshState
): IntOffset = when (style) {
    SwipeRefreshStyle.Translate, SwipeRefreshStyle.FixedBehind -> {
        IntOffset(0, state.indicatorOffset.toInt())
    }
    else -> IntOffset(0, 0)
}