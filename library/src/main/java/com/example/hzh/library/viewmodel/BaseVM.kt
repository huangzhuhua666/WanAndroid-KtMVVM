package com.example.hzh.library.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * Create by hzh on 2019/09/12.
 */
open class BaseVM : ViewModel() {

    /**
     * 页码
     */
    protected var pageNo = 0

    /**
     * 标记是否已加载完数据
     */
    val isFinish = MutableLiveData(false)

    /**
     * 标记是否需要显示loading动画
     */
    protected val _isShowLoading = MutableLiveData(false)
    val isShowLoading: LiveData<Boolean> = _isShowLoading

    /**
     * 标记是否已没有更多数据
     */
    protected val _isOver = MutableLiveData(false)
    val isOver: LiveData<Boolean> = _isOver

    /**
     * toast信息
     */
    protected val _toastTip = MutableLiveData<@androidx.annotation.StringRes Int>()
    val toastTip: LiveData<Int> = _toastTip

    /**
     * 捕获的异常
     */
    private val _exception = MutableLiveData<Throwable>()
    val exception: LiveData<Throwable> = _exception

    /**
     * 开启协程
     * @param tryBlock io线程
     * @param catchBlock 主线程
     * @param finallyBlock 主线程
     */
    fun doOnIO(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
        finallyBlock: suspend CoroutineScope.() -> Unit = {},
        handleCancellationExceptionManually: Boolean = false
    ) = viewModelScope.launch {
        tryCatch(
            tryBlock,
            catchBlock,
            finallyBlock,
            handleCancellationExceptionManually
        )
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean
    ) = coroutineScope {
        try {
            withContext(Dispatchers.IO) { tryBlock() }
        } catch (e: Throwable) {
            e.printStackTrace()
            if (e !is CancellationException || handleCancellationExceptionManually) {
                catchBlock(e)
                _exception.value = e
            } else throw e
        } finally {
            finallyBlock()
        }
    }

    /**
     * 刷新数据
     * @param isRefresh 标记是否需要loading动画
     */
    open fun getInitData(isRefresh: Boolean) {
        pageNo = 0
        _isShowLoading.value = !isRefresh
    }

    /**
     * 加载更多数据
     */
    open fun loadData() {
        ++pageNo
    }
}