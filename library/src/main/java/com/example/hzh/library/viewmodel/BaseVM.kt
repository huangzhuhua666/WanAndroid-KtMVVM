package com.example.hzh.library.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * Create by hzh on 2019/09/12.
 */
open class BaseVM : ViewModel() {

    protected var pageNo = 0

    var isLoadMore = false

    val isShowLoading = MutableLiveData(false)
    val isOver = MutableLiveData(false)
    val exception = MutableLiveData<Throwable>()

    fun doOnIO(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {},
        handleCancellationExceptionManually: Boolean = false
    ) {
        viewModelScope.launch {
            tryCatch(
                tryBlock,
                catchBlock,
                finallyBlock,
                handleCancellationExceptionManually
            )
        }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) {
        coroutineScope {
            try {
                withContext(Dispatchers.IO) { tryBlock() }
            } catch (e: Throwable) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    catchBlock(e)
                    exception.value = e
                } else throw e
            } finally {
                finallyBlock()
            }
        }
    }

    open fun getInitData() {
        isLoadMore = false
        pageNo = 0
    }

    open fun loadData() {
        isLoadMore = true
        ++pageNo
    }
}