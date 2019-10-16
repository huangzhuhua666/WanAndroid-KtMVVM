package com.example.hzh.library.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.hzh.library.animator.dsl.ValueAnim
import com.example.hzh.library.animator.dsl.valueAnim
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/10/8.
 */
class TriangleLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), LifecycleObserver {

    private var mCX = 0
    private var mCY = 0

    private val mEdge = 160

    private val mTriangles by lazy { Array(4) { Triangle() } }

    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL } }
    private val mPath by lazy { Path() }
    private var mCurrStatus = Status.MID_LOADING
    private var mAnim: ValueAnim? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wrapWidth = 500
        val wrapHeight = 500

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        layoutParams.run {
            if (width == ViewGroup.LayoutParams.WRAP_CONTENT &&
                height == ViewGroup.LayoutParams.WRAP_CONTENT
            )
                setMeasuredDimension(wrapWidth, wrapHeight)
            else if (width == ViewGroup.LayoutParams.WRAP_CONTENT)
                setMeasuredDimension(wrapWidth, heightSize)
            else if (height == ViewGroup.LayoutParams.WRAP_CONTENT)
                setMeasuredDimension(widthSize, wrapHeight)
        }

        mCX = measuredWidth / 2
        mCY = measuredHeight / 2
        initTriangles()
    }

    private fun initTriangles() {
        mCurrStatus = Status.MID_LOADING

        val offset = sqrt(mEdge.toFloat().pow(2) - (mEdge / 2.0f).pow(2)).toInt()

        val triangle1 = mTriangles[0].apply {
            startX = mCX + offset / 2
            startY = mCY + mEdge / 2
            endX1 = mCX + offset / 2
            endY1 = mCY - mEdge / 2
            endX2 = mCX - offset / 2
            endY2 = mCY
            currX1 = startX
            currY1 = startY
            currX2 = startX
            currY2 = startY
            color = Color.parseColor("#be8cd5")
        }

        mTriangles[1].apply {
            startX = triangle1.endX2
            startY = triangle1.endY2
            endX1 = triangle1.endX1
            endY1 = triangle1.endY1
            endX2 = startX
            endY2 = startY - mEdge
            color = Color.parseColor("#fcb131")
        }

        mTriangles[2].apply {
            startX = triangle1.endX1
            startY = triangle1.endY1
            endX1 = startX
            endY1 = startY + mEdge
            endX2 = startX + offset
            endY2 = startY + mEdge / 2
            color = Color.parseColor("#67c6ca")
        }

        mTriangles[3].apply {
            startX = triangle1.startX
            startY = triangle1.startY
            endX1 = triangle1.endX2
            endY1 = triangle1.endY2
            endX2 = triangle1.endX2
            endY2 = triangle1.endY2 + mEdge
            color = Color.parseColor("#eb7583")
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mTriangles.forEach {
            mPath.run {
                reset()
                moveTo(it.startX.toFloat(), it.startY.toFloat())
                lineTo(it.currX1.toFloat(), it.currY1.toFloat())
                lineTo(it.currX2.toFloat(), it.currY2.toFloat())
                close()
            }

            mPaint.color = it.color

            canvas?.drawPath(mPath, mPaint)

            if (mCurrStatus == Status.MID_LOADING) return
        }
    }

    fun startAnim() {
        initTriangles()

        mAnim?.cancel()

        mAnim = valueAnim {
            values = arrayOf(0f, 1f)
            duration = 360
            repeatCount = -1
            repeatMode = ValueAnimator.RESTART

            onRepeat = {
                mCurrStatus = when (mCurrStatus) {
                    Status.MID_LOADING -> Status.FIRST_LOADING
                    Status.FIRST_LOADING -> Status.SECOND_LOADING
                    Status.SECOND_LOADING -> Status.THIRD_LOADING
                    Status.THIRD_LOADING -> {
                        reverseAnim()
                        Status.LOAD_COMPLETE
                    }
                    Status.LOAD_COMPLETE -> Status.THIRD_DISMISS
                    Status.THIRD_DISMISS -> Status.FIRST_DISMISS
                    Status.FIRST_DISMISS -> Status.SECOND_DISMISS
                    Status.SECOND_DISMISS -> Status.MID_DISMISS
                    Status.MID_DISMISS -> {
                        reverseAnim()
                        Status.MID_LOADING
                    }
                }
            }

            action = {
                it as Float

                val fraction = when (mCurrStatus) {
                    Status.FIRST_DISMISS, Status.SECOND_DISMISS,
                    Status.THIRD_DISMISS, Status.MID_DISMISS -> 1 - it
                    else -> it
                }

                val triangle = when (mCurrStatus) {
                    Status.MID_LOADING, Status.MID_DISMISS -> mTriangles[0]
                    Status.FIRST_LOADING, Status.FIRST_DISMISS -> mTriangles[1]
                    Status.SECOND_LOADING, Status.SECOND_DISMISS -> mTriangles[2]
                    Status.THIRD_LOADING, Status.THIRD_DISMISS -> mTriangles[3]
                    else -> mTriangles[0]
                }

                if (mCurrStatus != Status.LOAD_COMPLETE) {
                    triangle.apply {
                        currX1 = startX + (fraction * (endX1 - startX)).toInt()
                        currY1 = startY + (fraction * (endY1 - startY)).toInt()
                        currX2 = startX + (fraction * (endX2 - startX)).toInt()
                        currY2 = startY + (fraction * (endY2 - startY)).toInt()
                    }
                    invalidate()
                }
            }
        }.apply { start() }
    }

    fun stopAnim() {
        mAnim?.cancel()
        mAnim = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        startAnim()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopAnim()
    }

    private fun reverseAnim() {
        mTriangles.forEach {
            val startX = it.startX
            val startY = it.startY

            it.startX = it.endX1
            it.startY = it.endY1
            it.endX1 = startX
            it.endY1 = startY

            it.currX1 = it.endX1
            it.currY1 = it.endY1
        }
    }

    private class Triangle {

        var startX = 0
        var startY = 0
        var currX1 = 0
        var currY1 = 0
        var currX2 = 0
        var currY2 = 0
        var endX1 = 0
        var endY1 = 0
        var endX2 = 0
        var endY2 = 0
        var color by Delegates.notNull<Int>()
    }

    private enum class Status {
        MID_LOADING,
        FIRST_LOADING,
        SECOND_LOADING,
        THIRD_LOADING,
        LOAD_COMPLETE,
        THIRD_DISMISS,
        FIRST_DISMISS,
        SECOND_DISMISS,
        MID_DISMISS
    }
}