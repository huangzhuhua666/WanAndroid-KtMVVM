package com.example.hzh.library.extension

import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener

/**
 * Create by hzh on 2019/08/07.
 */
fun SmartRefreshLayout.setListener(init: RefreshListenerHelper.() -> Unit) {
    val listener = RefreshListenerHelper()
    listener.init()
    setOnMultiPurposeListener(listener)
}

private typealias FooterMoving = (RefreshFooter?, Boolean, Float, Int, Int, Int) -> Unit
private typealias HeaderStartAnimator = (RefreshHeader?, Int, Int) -> Unit
private typealias FooterReleased = (RefreshFooter?, Int, Int) -> Unit
private typealias StateChanged = (RefreshLayout, RefreshState, RefreshState) -> Unit
private typealias HeaderMoving = (RefreshHeader?, Boolean, Float, Int, Int, Int) -> Unit
private typealias FooterFinish = (RefreshFooter?, Boolean) -> Unit
private typealias FooterStartAnimator = (RefreshFooter?, Int, Int) -> Unit
private typealias HeaderReleased = (RefreshHeader?, Int, Int) -> Unit
private typealias LoadMore = (RefreshLayout) -> Unit
private typealias Refresh = (RefreshLayout) -> Unit
private typealias HeaderFinish = (RefreshHeader?, Boolean) -> Unit

class RefreshListenerHelper : OnMultiPurposeListener {

    private var footerMoving: FooterMoving? = null
    private var headerStartAnimator: HeaderStartAnimator? = null
    private var footerReleased: FooterReleased? = null
    private var stateChanged: StateChanged? = null
    private var headerMoving: HeaderMoving? = null
    private var footerFinish: FooterFinish? = null
    private var footerStartAnimator: FooterStartAnimator? = null
    private var headerReleased: HeaderReleased? = null
    private var loadMore: LoadMore? = null
    private var refresh: Refresh? = null
    private var headerFinish: HeaderFinish? = null

    fun onFooterMoving(onFooterMoving: FooterMoving) {
        footerMoving = onFooterMoving
    }

    fun onHeaderStartAnimator(onHeaderStartAnimator: HeaderStartAnimator) {
        headerStartAnimator = onHeaderStartAnimator
    }

    fun onFooterReleased(onFooterReleased: FooterReleased) {
        footerReleased = onFooterReleased
    }

    fun onStateChanged(onStateChanged: StateChanged) {
        stateChanged = onStateChanged
    }

    fun onHeaderMoving(onHeaderMoving: HeaderMoving) {
        headerMoving = onHeaderMoving
    }

    fun onFooterFinish(onFooterFinish: FooterFinish) {
        footerFinish = onFooterFinish
    }

    fun onFooterStartAnimator(onFooterStartAnimator: FooterStartAnimator) {
        footerStartAnimator = onFooterStartAnimator
    }

    fun onHeaderReleased(onHeaderReleased: HeaderReleased) {
        headerReleased = onHeaderReleased
    }

    fun onLoadMore(onLoadMore: LoadMore) {
        loadMore = onLoadMore
    }

    fun onRefresh(onRefresh: Refresh) {
        refresh = onRefresh
    }

    fun onHeaderFinish(onHeaderFinish: HeaderFinish) {
        headerFinish = onHeaderFinish
    }

    override fun onFooterMoving(
        footer: RefreshFooter?,
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        footerHeight: Int,
        maxDragHeight: Int
    ) {
        footerMoving?.invoke(footer, isDragging, percent, offset, footerHeight, maxDragHeight)
    }

    override fun onHeaderStartAnimator(
        header: RefreshHeader?,
        headerHeight: Int,
        maxDragHeight: Int
    ) {
        headerStartAnimator?.invoke(header, headerHeight, maxDragHeight)
    }

    override fun onFooterReleased(
        footer: RefreshFooter?,
        footerHeight: Int,
        maxDragHeight: Int
    ) {
        footerReleased?.invoke(footer, footerHeight, maxDragHeight)
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        stateChanged?.invoke(refreshLayout, oldState, newState)
    }

    override fun onHeaderMoving(
        header: RefreshHeader?,
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        headerHeight: Int,
        maxDragHeight: Int
    ) {
        headerMoving?.invoke(header, isDragging, percent, offset, headerHeight, maxDragHeight)
    }

    override fun onFooterFinish(
        footer: RefreshFooter?,
        success: Boolean
    ) {
        footerFinish?.invoke(footer, success)
    }

    override fun onFooterStartAnimator(
        footer: RefreshFooter?,
        footerHeight: Int,
        maxDragHeight: Int
    ) {
        footerStartAnimator?.invoke(footer, footerHeight, maxDragHeight)
    }

    override fun onHeaderReleased(
        header: RefreshHeader?,
        headerHeight: Int,
        maxDragHeight: Int
    ) {
        headerReleased?.invoke(header, headerHeight, maxDragHeight)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        loadMore?.invoke(refreshLayout)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh?.invoke(refreshLayout)
    }

    override fun onHeaderFinish(
        header: RefreshHeader?,
        success: Boolean
    ) {
        headerFinish?.invoke(header, success)
    }
}