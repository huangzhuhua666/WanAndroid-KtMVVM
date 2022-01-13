package com.example.hzh.ktmvvm.widget.refreshlayout

enum class SwipeRefreshStyle {

    /**
     * 平移，即内容与 Header 一起向下滑动，Translate 为默认样式
     */
    Translate,

    /**
     * 固定在背后，即内容向下滑动，Header 不动
     */
    FixedBehind,

    /**
     * 固定在前面，即 Header 固定在前，Header 与 Content 都不滑动
     */
    FixedFront,

    /**
     * 内容固定，Header 向下滑动，即官方样式
     */
    FixedContent
}