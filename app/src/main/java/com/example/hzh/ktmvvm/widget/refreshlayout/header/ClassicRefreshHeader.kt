package com.example.hzh.ktmvvm.widget.refreshlayout.header

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.widget.refreshlayout.RefreshHeaderState
import com.example.hzh.ktmvvm.widget.refreshlayout.SwipeRefreshState
import com.example.hzh.ktmvvm.widget.refreshlayout.header.painter.ArrowPainter
import com.example.hzh.ktmvvm.widget.refreshlayout.header.painter.ProgressPainter

/**
 * @author huangzhuhua
 * @date 2022/1/10
 */
@Composable
fun ClassicRefreshHeader(state: SwipeRefreshState) {
    val text = when (state.headerState) {
        RefreshHeaderState.PullDownToRefresh -> stringResource(R.string.swipe_pull_refresh)
        RefreshHeaderState.Refreshing -> stringResource(R.string.swipe_refreshing)
        else -> stringResource(R.string.swipe_release_to_refresh)
    }

    val angle = remember { Animatable(0f) }
    LaunchedEffect(key1 = state.headerState) {
        if (state.headerState == RefreshHeaderState.ReleaseToRefresh) {
            angle.animateTo(180f)
        } else {
            angle.animateTo(0f)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (state.headerState == RefreshHeaderState.Refreshing) {
            RefreshingView()
        } else {
            val arrowPainter = remember { ArrowPainter() }
            Image(
                painter = arrowPainter,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(angle.value)
            )
        }

        Text(
            text = text,
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .wrapContentSize()
                .clipToBounds()
                .padding(horizontal = 16.dp, vertical = 0.dp)
        )
    }
}

@Composable
private fun RefreshingView() {
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