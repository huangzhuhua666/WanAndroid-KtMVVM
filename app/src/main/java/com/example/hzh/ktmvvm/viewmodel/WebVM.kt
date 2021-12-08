package com.example.hzh.ktmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Create by hzh on 2021/11/24
 */
class WebVM(
    originTitle: String
) : ViewModel() {

    private val _title = MutableStateFlow(originTitle)
    val title: StateFlow<String>
        get() = _title
    
    fun updateTitle(title: String) {
        _title.value = title
    }
}

class WebVMFactory(
    private val originTitle: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        WebVM(originTitle = originTitle) as T
}