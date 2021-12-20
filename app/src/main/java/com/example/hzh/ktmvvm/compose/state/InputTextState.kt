package com.example.hzh.ktmvvm.compose.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * Create by hzh on 2021/12/7
 */
class InputTextState(initial: String) {

    companion object {

        val Saver: Saver<InputTextState, *> = listSaver(
            save = { listOf(it.text) },
            restore = { InputTextState(initial = it[0]) }
        )
    }

    var text by mutableStateOf(initial)
        private set

    val isTextEmpty: Boolean
        get() = text.trim().isEmpty()

    fun updateText(str: String) {
        text = str
    }
}

@Composable
fun rememberInputTextState(initialText: String): InputTextState = rememberSaveable(
    initialText,
    saver = InputTextState.Saver
) {
    InputTextState(initial = initialText)
}