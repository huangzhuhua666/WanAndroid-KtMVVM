package com.example.hzh.ktmvvm.compose.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

/**
 * Create by hzh on 2021/12/8
 */

const val FAST_CLICK_DURATION = 600L

fun Modifier.fastClickable(
    duration: Long = FAST_CLICK_DURATION,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    indication: Indication? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "fastClickable"
        properties["duration"] = duration
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    }
) {
    var lastClickTime = remember { 0L }

    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        indication = indication,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime <= duration) {
            return@clickable
        }

        onClick()
        lastClickTime = currentTime
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.fastCombinedClickable(
    duration: Long = FAST_CLICK_DURATION,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    onLongClickLabel: String? = null,
    role: Role? = null,
    indication: Indication? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "fastCombinedClickable"
        properties["duration"] = duration
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
        properties["onDoubleClick"] = onDoubleClick
        properties["onLongClick"] = onLongClick
        properties["onLongClickLabel"] = onLongClickLabel
    }
) {
    var lastClickTime = remember { 0L }

    Modifier.combinedClickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        indication = indication,
        interactionSource = remember { MutableInteractionSource() },
        onLongClickLabel = onLongClickLabel,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime <= duration) {
            return@combinedClickable
        }

        onClick()
        lastClickTime = currentTime
    }
}