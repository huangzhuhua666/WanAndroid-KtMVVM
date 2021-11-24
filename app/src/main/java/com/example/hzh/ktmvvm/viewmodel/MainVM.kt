package com.example.hzh.ktmvvm.viewmodel

import com.example.hzh.library.viewmodel.BaseVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Create by hzh on 2021/11/24
 */
class MainVM : BaseVM() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String>
        get() = _title

    fun updateTitle(title: String) {
        _title.value = title
    }
}