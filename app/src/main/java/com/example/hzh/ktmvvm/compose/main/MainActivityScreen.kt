package com.example.hzh.ktmvvm.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.compose.common.MyIconButton

/**
 * Create by hzh on 2021/11/24
 */
@Composable
fun MainTitle(
    title: String,
    onDrawerClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(49.dp)
            .background(color = colorResource(R.color.appColor)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val btnModifier = Modifier.size(49.dp)

        MyIconButton(
            modifier = btnModifier,
            onClick = onDrawerClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_drawer),
                contentDescription = null,
                tint = Color.White
            )
        }

        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            color = Color.White,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        MyIconButton(
            modifier = btnModifier,
            onClick = onSearchClick
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search_white),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
fun PreviewMainTitle() {
    Surface {
        MainTitle(title = "WanAndroid", onDrawerClick = { }, onSearchClick = { })
    }
}