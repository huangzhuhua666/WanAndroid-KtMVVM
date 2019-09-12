package com.example.hzh.library.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * Create by hzh on 2019/09/12.
 */
abstract class BaseVM : ViewModel(), CoroutineScope by MainScope() {

    override fun onCleared() {
        cancel()
        super.onCleared()
    }
}