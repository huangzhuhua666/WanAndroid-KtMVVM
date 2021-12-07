package com.example.hzh.ktmvvm.compose.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Create by hzh on 2021/12/6
 */
class ProgressState(max: Int = 100, initial: Int) {

    private val max: Int = if (max <= 0) 100 else max
    private val initialProgress: Int = if (initial < 0) 0 else initial

    companion object {

        val Saver: Saver<ProgressState, *> = listSaver(
            save = { listOf(it.max, it.progress) },
            restore = { ProgressState(max = it[0], initial = it[1]) }
        )
    }

    var progress by mutableStateOf(initialProgress)

    val ratio: Float
        get() = progress.toFloat() / max

    val isShowProgressBar: Boolean
        get() = progress in 0 until max
}

@Composable
fun rememberProgressState(max: Int): ProgressState = rememberSaveable(
    max,
    saver = ProgressState.Saver
) {
    ProgressState(max = max, initial = 0)
}