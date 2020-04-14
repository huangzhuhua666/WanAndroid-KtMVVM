package com.example.hzh.library.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.bundleOf
import com.example.hzh.library.R
import com.example.hzh.library.extension.no
import com.example.hzh.library.extension.yes
import kotlin.math.max
import kotlin.math.min

/**
 * Create by hzh on 2020/4/2.
 */
class RImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val STATE_INSTANCE = "state_instance"
        private const val STATE_SHAPE = "state_shape"
        private const val STATE_RADII = "state_radii"
        private const val STATE_BORDER_WIDTH = "state_border_width"
        private const val STATE_BORDER_COLOR = "state_border_color"
    }

    enum class Shape {

        /**
         * 圆角
         */
        ROUND_CORNER,

        /**
         * 圆形
         */
        CIRCLE
    }

    private val mBitmapPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
    }
    private val mBorderPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = borderColor
            strokeCap = Paint.Cap.ROUND
            strokeWidth = borderWidth
        }
    }

    private val mMatrix by lazy { Matrix() }

    private val mBorderPath by lazy { Path() }
    private val mShapePath by lazy { Path() }
    private val mShapeRegion by lazy { Region() }

    private var mWidth: Int = 200
    private var mHeight: Int = 200
    private var mRadius: Float = 100f
    private var isResetAllRadius: Boolean = false

    var shape: Shape = Shape.ROUND_CORNER
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 设置左上圆角x
     */
    var leftTopRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    /**
     * 设置左上圆角y
     */
    var leftTopRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    /**
     * 设置右上圆角x
     */
    var rightTopRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    /**
     * 设置右上圆角y
     */
    var rightTopRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    /**
     * 设置左下圆角x
     */
    var leftBottomRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    /**
     * 设置左下圆角y
     */
    var leftBottomRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    /**
     * 设置右下圆角x
     */
    var rightBottomRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    /**
     * 设置右下圆角y
     */
    var rightBottomRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) invalidate()
        }

    var borderWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var borderColor: Int = Color.parseColor("#ff9900")
        set(value) {
            field = value
            invalidate()
        }

    init {
        scaleType = ScaleType.CENTER_CROP

        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.RImageView).run {
            shape = when (getInt(R.styleable.RImageView_shape, 0)) {
                0 -> Shape.ROUND_CORNER // 圆角矩形
                1 -> Shape.CIRCLE // 圆形
                else -> Shape.ROUND_CORNER
            }

            val radii = getDimension(R.styleable.RImageView_radii, 0f)
            if (radii >= 0f) setRadii(radii)

            // 左上x
            val leftTopX = getDimension(R.styleable.RImageView_leftTopRadiusX, 0f)
            if (radii <= 0f && leftTopX >= 0f) leftTopRadiusX = leftTopX

            // 左上y
            val leftTopY = getDimension(R.styleable.RImageView_leftTopRadiusY, 0f)
            if (radii <= 0f && leftTopY >= 0f) leftTopRadiusY = leftTopY

            // 左下x
            val leftBottomX = getDimension(R.styleable.RImageView_leftBottomRadiusX, 0f)
            if (radii == 0f && leftBottomX >= 0f) leftBottomRadiusX = leftBottomX

            // 左下y
            val leftBottomY = getDimension(R.styleable.RImageView_leftBottomRadiusY, 0f)
            if (radii == 0f && leftBottomY >= 0f) leftBottomRadiusY = leftBottomY

            // 右上x
            val rightTopX = getDimension(R.styleable.RImageView_rightTopRadiusX, 0f)
            if (radii == 0f && rightTopX >= 0f) rightTopRadiusX = rightTopX

            // 右上y
            val rightTopY = getDimension(R.styleable.RImageView_rightTopRadiusY, 0f)
            if (radii == 0f && rightTopY >= 0f) rightTopRadiusY = rightTopY

            // 右下x
            val rightBottomX = getDimension(R.styleable.RImageView_rightBottomRadiusX, 0f)
            if (radii == 0f && rightBottomX >= 0f) rightBottomRadiusX = rightBottomX

            // 右下y
            val rightBottomY = getDimension(R.styleable.RImageView_rightBottomRadiusY, 0f)
            if (radii == 0f && rightBottomY >= 0f) rightBottomRadiusY = rightBottomY

            // 边框宽度
            borderWidth = getDimension(R.styleable.RImageView_borderWidth, 0f)

            // 边框颜色
            borderColor = getColor(R.styleable.RImageView_borderColor, Color.parseColor("#ff9900"))

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (shape) {
            Shape.ROUND_CORNER -> {
                mWidth = measuredWidth
                mHeight = measuredHeight
            }
            Shape.CIRCLE -> {
                mWidth = min(measuredWidth, measuredHeight)
                mRadius = mWidth / 2.toFloat()
                mHeight = mWidth
            }
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val totalRegion = Region(0, 0, w, h)
        // 确定最终大小
        when (shape) {
            Shape.ROUND_CORNER -> {
                mWidth = w
                mHeight = h
                createRoundRectPath()
            }
            Shape.CIRCLE -> {
                mWidth = min(w, h)
                mRadius = mWidth / 2.toFloat()
                mHeight = mWidth
                createCirclePath()
            }
        }

        mShapeRegion.setPath(mShapePath, totalRegion)
    }

    /**
     * 创建一个圆角矩形Path
     */
    private fun createRoundRectPath() {
        (borderWidth > 0).yes { // 有边框
            mShapePath.addRoundRect(
                borderWidth, borderWidth,
                mWidth - borderWidth, mHeight - borderWidth,
                floatArrayOf(
                    leftTopRadiusX - borderWidth, leftTopRadiusY - borderWidth,
                    rightTopRadiusX - borderWidth, rightTopRadiusY - borderWidth,
                    rightBottomRadiusX - borderWidth, rightBottomRadiusY - borderWidth,
                    leftBottomRadiusX - borderWidth, leftBottomRadiusY - borderWidth
                ),
                Path.Direction.CW
            )

            // 边框的Path
            mBorderPath.addRoundRect(
                borderWidth / 2, borderWidth / 2,
                mWidth - borderWidth / 2,
                mHeight - borderWidth / 2,
                floatArrayOf(
                    leftTopRadiusX - borderWidth / 2, leftTopRadiusY - borderWidth / 2,
                    rightTopRadiusX - borderWidth / 2, rightTopRadiusY - borderWidth / 2,
                    rightBottomRadiusX - borderWidth / 2, rightBottomRadiusY - borderWidth / 2,
                    leftBottomRadiusX - borderWidth / 2, leftBottomRadiusY - borderWidth / 2
                ),
                Path.Direction.CW
            )
        }.no { // 无边框
            mShapePath.addRoundRect(
                0f, 0f,
                mWidth.toFloat(), mHeight.toFloat(),
                floatArrayOf(
                    leftTopRadiusX, leftTopRadiusY,
                    rightTopRadiusX, rightTopRadiusY,
                    rightBottomRadiusX, rightBottomRadiusY,
                    leftBottomRadiusX, leftBottomRadiusY
                ),
                Path.Direction.CW
            )
        }
    }

    /**
     * 创建一个圆形Path
     */
    private fun createCirclePath() {
        (borderWidth > 0).yes { // 有边框
            mShapePath.addCircle(
                mRadius, mRadius,
                mRadius - borderWidth,
                Path.Direction.CW
            )

            // 边框的Path
            mBorderPath.addCircle(
                mRadius, mRadius,
                mRadius - borderWidth / 2,
                Path.Direction.CW
            )
        }.no { mShapePath.addCircle(mRadius, mRadius, mRadius, Path.Direction.CW) } // 无边框
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            mBitmapPaint.shader = getBitmapShader() // 通过BitmapShader画图片

            (borderWidth > 0).yes { drawPath(mBorderPath, mBorderPaint) } // 画边框？

            // 画Bitmap
            drawPath(mShapePath, mBitmapPaint)
        }
    }

    /**
     * 创建一个BitmapShader
     */
    private fun getBitmapShader(): BitmapShader? = drawable?.run {
        drawableToBitmap(this).let {
            BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
                mMatrix.reset()

                // 缩放
                val scale = when (shape) {
                    Shape.ROUND_CORNER -> {
                        (mWidth != it.width && mHeight != it.height)
                            .yes { max(mWidth.toFloat() / it.width, mHeight.toFloat() / it.height) }
                            .no { 1.0f }
                    }
                    Shape.CIRCLE -> mWidth.toFloat() / min(it.width, it.height)
                }

                mMatrix.setScale(scale, scale)
                setLocalMatrix(mMatrix)
            }
        }
    }

    /**
     * Drawable转Bitmap
     *
     * @param drawable
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap = when (drawable) {
        is BitmapDrawable -> drawable.bitmap // BitmapDrawable，直接取Bitmap
        else -> drawable.let {
            // 通过drawable.draw(canvas)创建一个Bitmap
            Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
                .apply {
                    it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                    Canvas(this).run { it.draw(this) }
                }
        }
    }

    /**
     * 统一设置四个角的圆角
     *
     * @param radii 圆角大小
     */
    private fun setRadii(radii: Float) {
        if (radii < 0) return

        isResetAllRadius = true
        leftTopRadiusX = radii
        leftTopRadiusY = radii
        leftBottomRadiusX = radii
        leftBottomRadiusY = radii
        rightTopRadiusX = radii
        rightTopRadiusY = radii
        rightBottomRadiusX = radii
        rightBottomRadiusY = radii
        invalidate()
        isResetAllRadius = false
    }

    /**
     * 设置边框颜色
     *
     * @param color color
     */
    fun setBorderColor(color: String) {
        borderColor = Color.parseColor(color)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            // 判断点击事件坐标是不是在图形的Region里面
            if (action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN)
                return mShapeRegion.contains(x.toInt(), y.toInt())
                        && super.dispatchTouchEvent(event)
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onSaveInstanceState(): Parcelable? = bundleOf(
        STATE_INSTANCE to super.onSaveInstanceState(),
        STATE_SHAPE to when (shape) {
            Shape.ROUND_CORNER -> 0
            Shape.CIRCLE -> 1
        },
        STATE_RADII to floatArrayOf(
            leftTopRadiusX, leftTopRadiusY,
            rightTopRadiusX, rightTopRadiusY,
            leftBottomRadiusX, leftBottomRadiusY,
            rightBottomRadiusX, rightBottomRadiusY
        ),
        STATE_BORDER_WIDTH to borderWidth,
        STATE_BORDER_COLOR to borderColor
    )

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is Bundle) {
            super.onRestoreInstanceState(state)
            return
        }

        with(state) {
            super.onRestoreInstanceState(getParcelable(STATE_INSTANCE))

            shape = when (getInt(STATE_SHAPE, 0)) {
                0 -> Shape.ROUND_CORNER
                1 -> Shape.CIRCLE
                else -> Shape.ROUND_CORNER
            }

            getFloatArray(STATE_RADII)?.let {
                isResetAllRadius = true
                leftTopRadiusX = it[0]
                leftTopRadiusY = it[1]
                rightTopRadiusX = it[2]
                rightTopRadiusY = it[3]
                leftBottomRadiusX = it[4]
                leftBottomRadiusY = it[5]
                rightBottomRadiusX = it[6]
                rightBottomRadiusY = it[7]
                invalidate()
                isResetAllRadius = false
            }

            borderWidth = getFloat(STATE_BORDER_WIDTH, 0f)
            borderColor = getInt(STATE_BORDER_COLOR, Color.parseColor("#ff9900"))
        }
    }
}