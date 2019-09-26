package com.example.hzh.library.widget

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager

/**
 * Create by hzh on 2019/9/24.
 */
class TabIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {

        private const val STATE_INSTANCE = "instance_state"
        private const val STATE_ITEM = "state_item"
    }

    var viewpager: ViewPager? = null
        set(value) {
            field = value
            init()
        }

    private var isInit = false
    private var mCurrentIndex = 0
    private var mListener: ((Int) -> Unit)? = null
    private val mTabList by lazy { mutableListOf<TabView>() }

    init {
        post { checkInit() }
    }

    private fun checkInit() {
        if (!isInit) init()
    }

    private fun init() {
        isInit = true

        val count = childCount

        viewpager?.run {
            adapter.let {
                if (it == null) throw NullPointerException("viewpager的adapter为null")

                if (it.count != count) throw throw IllegalArgumentException("子View数量必须和ViewPager条目数量一致")
            }

            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    // 滑动时的透明度动画
                    if (positionOffset > 0) {
                        mTabList[position].mAlpha = 1 - positionOffset
                        mTabList[position + 1].mAlpha = positionOffset
                    }
                    // 滑动时保存当前按钮索引
                    mCurrentIndex = position
                }

                override fun onPageSelected(position: Int) {
                    resetState()
                    mTabList[position].mAlpha = 1.0f
                    mCurrentIndex = position
                }
            })
        }

        for (i in 0 until childCount) {
            getChildAt(i)?.let {
                if (it is TabView) {
                    mTabList.add(it)
                    it.setOnClickListener {
                        //点击前先重置所有按钮的状态
                        resetState()
                        mTabList[i].mAlpha = 1.0f

                        mListener?.invoke(i)

                        // 不能使用平滑滚动，否者颜色改变会乱
                        viewpager?.setCurrentItem(i, false)

                        // 点击是保存当前按钮索引
                        mCurrentIndex = i
                    }
                } else throw IllegalArgumentException("TabIndicator的子View必须是TabView")
            }
        }

        mTabList[mCurrentIndex].mAlpha = 1.0f
    }

    fun setOnTabChangedListener(listener: (Int) -> Unit) {
        mListener = listener
        checkInit()
    }

    fun getCurrentTab(): TabView {
        checkInit()
        return mTabList[mCurrentIndex]
    }

    fun getTab(index: Int): TabView {
        checkInit()
        return mTabList[index]
    }

    fun removeAllBadge() {
        checkInit()
        mTabList.forEach { it.removeShow() }
    }

    fun setCurrentTab(index: Int) {
        mTabList[index].performClick()
    }

    /**
     * 重置所有按钮的状态
     */
    private fun resetState() {
        mTabList.forEach { it.mAlpha = 0.0f }
    }

    override fun onSaveInstanceState(): Parcelable? = bundleOf(
        STATE_INSTANCE to super.onSaveInstanceState(),
        STATE_ITEM to mCurrentIndex
    )

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            mCurrentIndex = state.getInt(STATE_ITEM)

            if (mTabList.isEmpty()) {
                super.onRestoreInstanceState(state.getParcelable(STATE_INSTANCE))
                return
            }

            //重置所有按钮状态
            resetState()

            //恢复点击的条目颜色
            mTabList[mCurrentIndex].mAlpha = 1.0f
            super.onRestoreInstanceState(state.getParcelable(STATE_INSTANCE))
        } else super.onRestoreInstanceState(state)
    }
}