package com.example.hzh.ktmvvm.compose.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.compose.util.fastCombinedClickable
import java.util.*

/**
 * Create by hzh on 2021/12/10
 */

/**
 * 随机字体颜色
 * @param bound rbg边界 0 ~ bound - 1
 */
@Composable
fun RandomColorTextItem(
    text: String,
    bound: Int = 235,
    onClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {
    BorderText(
        text = text,
        outerModifier = Modifier
            .padding(horizontal = 8.dp, vertical = 5.dp)
            .wrapContentSize()
            .background(Color.White)
            .fastCombinedClickable(
                onLongClick = { onLongClick?.invoke() }
            ) {
                onClick?.invoke()
            },
        innerModifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        fontSize = 14.sp,
        textColor = remember(text) { randomColor(bound) },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        border = BorderStroke(
            width = 1.dp,
            color = colorResource(R.color.color_4c4c4c)
        ),
        shape = RoundedCornerShape(36.dp)
    )
}

private fun randomColor(bound: Int = 235): Color = Color(
    red = Random().nextInt(bound),
    green = Random().nextInt(bound),
    blue = Random().nextInt(bound)
)