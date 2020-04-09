package com.example.hzh.library.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.example.hzh.library.R
import com.example.hzh.library.extension.no
import com.example.hzh.library.extension.yes

/**
 * Create by hzh on 2019/07/03.
 *
 * 可设置圆角、渐变色、边框、按下状态的颜色等
 */
class RTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {

        private const val TAG = "RTextView"
    }

    enum class GradientType {

        /**
         * 线性渐变（默认）
         */
        LINEAR,

        /**
         * 放射状渐变
         */
        RADIAL,

        /**
         * 扫描渐变
         */
        SWEEP
    }

    enum class GradientOrientation {

        /**
         * 渐变方向从上到下
         */
        TOP_BOTTOM,

        /**
         * 渐变方向从右上到左下
         */
        RT_LB,

        /**
         * 渐变方向从右到左
         */
        RIGHT_LEFT,

        /**
         * 渐变方向从右下到左上
         */
        RB_LT,

        /**
         * 渐变方向从下到上
         */
        BOTTOM_TOP,

        /**
         * 渐变方向从左下到右上
         */
        LB_RT,

        /**
         * 渐变方向从左到右（默认）
         */
        LEFT_RIGHT,

        /**
         * 渐变方向从左上到右下
         */
        LT_RB
    }

    private val mGdn by lazy { GradientDrawable() }
    private val mGdp by lazy { GradientDrawable() }

    private var isResetAllRadius: Boolean = false
    private var isGradientSolid: Boolean = false

    /**
     * 设置左上圆角x
     */
    var leftTopRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    /**
     * 设置左上圆角y
     */
    var leftTopRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    /**
     * 设置左下圆角x
     */
    var leftBottomRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    /**
     * 设置左下圆角y
     */
    var leftBottomRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    /**
     * 设置右上圆角x
     */
    var rightTopRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    /**
     * 设置右上圆角y
     */
    var rightTopRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    /**
     * 设置右下圆角x
     */
    var rightBottomRadiusX: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    /**
     * 设置右下圆角y
     */
    var rightBottomRadiusY: Float = 0f
        set(value) {
            if (value < 0) return

            field = value
            if (!isResetAllRadius) resetRadius()
        }

    @ColorInt
    var normalTextColor: Int = 0
        set(value) {
            field = value
            setTextStateListColor()
        }

    @ColorInt
    var pressTextColor: Int = 0
        set(value) {
            field = value
            setTextStateListColor()
        }

    init {
        val bg = StateListDrawable()
        bg.addState(intArrayOf(android.R.attr.state_pressed), mGdp)
        bg.addState(intArrayOf(), mGdn)
        background = bg
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.RTextView).run {
            // 圆角
            val radii = getDimension(R.styleable.RTextView_radii, 0f)
            if (radii >= 0f) setRadii(radii)

            // 左上x
            val leftTopX = getDimension(R.styleable.RTextView_leftTopRadiusX, 0f)
            if (radii <= 0f && leftTopX >= 0f) leftTopRadiusX = leftTopX

            // 左上y
            val leftTopY = getDimension(R.styleable.RTextView_leftTopRadiusY, 0f)
            if (radii <= 0f && leftTopY >= 0f) leftTopRadiusY = leftTopY


            // 左下x
            val leftBottomX = getDimension(R.styleable.RTextView_leftBottomRadiusX, 0f)
            if (radii <= 0f && leftBottomX >= 0f) leftBottomRadiusX = leftBottomX

            // 左下y
            val leftBottomY = getDimension(R.styleable.RTextView_leftBottomRadiusY, 0f)
            if (radii <= 0f && leftBottomY >= 0f) leftBottomRadiusY = leftBottomY

            // 右上x
            val rightTopX = getDimension(R.styleable.RTextView_rightTopRadiusX, 0f)
            if (radii <= 0f && rightTopX >= 0f) rightTopRadiusX = rightTopX

            // 右上y
            val rightTopY = getDimension(R.styleable.RTextView_rightTopRadiusY, 0f)
            if (radii <= 0f && rightTopY >= 0f) rightTopRadiusY = rightTopY

            // 右下x
            val rightBottomX = getDimension(R.styleable.RTextView_rightBottomRadiusX, 0f)
            if (radii <= 0f && rightBottomX >= 0f) rightBottomRadiusX = rightBottomX

            // 右下y
            val rightBottomY = getDimension(R.styleable.RTextView_rightBottomRadiusY, 0f)
            if (radii <= 0f && rightBottomY >= 0f) rightBottomRadiusY = rightBottomY

            // 字体颜色
            normalTextColor = getColor(R.styleable.RTextView_normalTextColor, currentTextColor)
            pressTextColor = getColor(R.styleable.RTextView_pressTextColor, normalTextColor)
            setTextStateListColor()

            // 正常状态的边框
            val normalStrokeWidth = getDimension(R.styleable.RTextView_normalStrokeWidth, 0f)
            val normalStrokeColor = getColor(R.styleable.RTextView_normalStrokeColor, 0)
            setNormalStroke((normalStrokeWidth + .5f).toInt(), normalStrokeColor)

            // 按下状态的边框
            val pressStrokeWidth =
                getDimension(R.styleable.RTextView_pressStrokeWidth, normalStrokeWidth)
            val pressStrokeColor =
                getColor(R.styleable.RTextView_pressStrokeColor, normalStrokeColor)
            setPressStroke((pressStrokeWidth + .5f).toInt(), pressStrokeColor)

            isGradientSolid = getBoolean(R.styleable.RTextView_isGradientSolid, false)
            isGradientSolid.yes { // 使用渐变色
                // 正常状态的渐变色
                val normalGradientStart = getColor(
                    R.styleable.RTextView_normalGradientStart,
                    0
                )
                val normalGradientEnd = getColor(
                    R.styleable.RTextView_normalGradientEnd,
                    normalGradientStart
                )
                setNormalGradient(normalGradientStart, normalGradientEnd)

                // 按下状态的渐变色
                val pressGradientStart = getColor(
                    R.styleable.RTextView_pressGradientStart,
                    normalGradientStart
                )
                val pressGradientEnd = getColor(
                    R.styleable.RTextView_pressGradientEnd,
                    pressGradientStart
                )
                setPressGradient(pressGradientStart, pressGradientEnd)

                // 渐变色的类型
                val gradientType = when (getInt(R.styleable.RTextView_gradientType, 0)) {
                    0 -> GradientType.LINEAR
                    1 -> GradientType.RADIAL
                    2 -> GradientType.SWEEP
                    else -> GradientType.LINEAR
                }
                setGradientType(gradientType)

                // 渐变色的方向
                val gradientOrientation = when (getInt(
                    R.styleable.RTextView_gradientOrientation,
                    6
                )) {
                    0 -> GradientOrientation.TOP_BOTTOM
                    1 -> GradientOrientation.RT_LB
                    2 -> GradientOrientation.RIGHT_LEFT
                    3 -> GradientOrientation.RB_LT
                    4 -> GradientOrientation.BOTTOM_TOP
                    5 -> GradientOrientation.LB_RT
                    6 -> GradientOrientation.LEFT_RIGHT
                    7 -> GradientOrientation.LT_RB
                    else -> GradientOrientation.LEFT_RIGHT
                }
                setGradientOrientation(gradientOrientation)

                val useLevel = getBoolean(R.styleable.RTextView_useLevel, false)
                setUseLevel(useLevel)
            }.no {
                // 不使用渐变色
                // 正常状态的填充色
                val normalSolid = getColor(R.styleable.RTextView_normalSolid, 0)
                setNormalSolid(normalSolid)

                // 按下状态的填充色
                val pressSolid = getColor(R.styleable.RTextView_pressSolid, normalSolid)
                setPressSolid(pressSolid)
            }

            recycle()
        }
    }

    /**
     * 统一设置四个角的圆角
     *
     * @param radii 圆角大小
     */
    fun setRadii(radii: Float) {
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
        resetRadius()
        isResetAllRadius = false
    }

    /**
     * 更新控件的圆角大小
     *
     * radii数组表示四个角：左上、右上、右下、左下
     *
     * 每个角对应X_Radius，Y_Radius
     */
    private fun resetRadius() {
        val radii = floatArrayOf(
            leftTopRadiusX, leftTopRadiusY,
            rightTopRadiusX, rightTopRadiusY,
            rightBottomRadiusX, rightBottomRadiusY,
            leftBottomRadiusX, leftBottomRadiusY
        )
        mGdn.cornerRadii = radii
        mGdp.cornerRadii = radii
        invalidate()
    }

    /**
     * 设置正常状态的文字颜色
     *
     * @param color color
     */
    fun setNormalTextColor(color: String) {
        normalTextColor = Color.parseColor(color)
    }

    /**
     * 设置按下状态的文字颜色
     *
     * @param color color
     */
    fun setPressTextColor(color: String) {
        pressTextColor = Color.parseColor(color)
    }

    /**
     * 更新文字的颜色
     */
    private fun setTextStateListColor() {
        val state = arrayOfNulls<IntArray>(2)
        state[0] = intArrayOf(android.R.attr.state_pressed)
        state[1] = intArrayOf()
        setTextColor(ColorStateList(state, intArrayOf(pressTextColor, normalTextColor)))
        invalidate()
    }

    /**
     * 设置正常状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setNormalStroke(width: Int, color: String) {
        setNormalStroke(width, Color.parseColor(color))
    }

    /**
     * 设置正常状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setNormalStroke(width: Int, @ColorInt color: Int) {
        mGdn.setStroke(width, color)
        invalidate()
    }

    /**
     * 设置按下状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setPressStroke(width: Int, color: String) {
        setPressStroke(width, Color.parseColor(color))
    }

    /**
     * 设置按下状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setPressStroke(width: Int, @ColorInt color: Int) {
        mGdp.setStroke(width, color)
        invalidate()
    }

    /**
     * 设置正常状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setNormalGradient(start: String, end: String) {
        setNormalGradient(Color.parseColor(start), Color.parseColor(end))
    }

    /**
     * 设置正常状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setNormalGradient(@ColorInt start: Int, @ColorInt end: Int) {
        mGdn.colors = intArrayOf(start, end)
        invalidate()
    }

    /**
     * 设置按下状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setPressGradient(start: String, end: String) {
        setPressGradient(Color.parseColor(start), Color.parseColor(end))
    }

    /**
     * 设置按下状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setPressGradient(@ColorInt start: Int, @ColorInt end: Int) {
        mGdp.colors = intArrayOf(start, end)
        invalidate()
    }

    /**
     * 设置渐变的类型，默认[GradientType.LINEAR]
     *
     * @param gradientType 渐变的类型：
     * [GradientType.LINEAR]，[GradientType.RADIAL]，[GradientType.SWEEP]
     */
    fun setGradientType(gradientType: GradientType) {
        when (gradientType) {
            GradientType.LINEAR -> {
                mGdn.gradientType = GradientDrawable.LINEAR_GRADIENT
                mGdp.gradientType = GradientDrawable.LINEAR_GRADIENT
            }
            GradientType.RADIAL -> {
                mGdn.gradientType = GradientDrawable.RADIAL_GRADIENT
                mGdp.gradientType = GradientDrawable.RADIAL_GRADIENT
            }
            GradientType.SWEEP -> {
                mGdn.gradientType = GradientDrawable.SWEEP_GRADIENT
                mGdp.gradientType = GradientDrawable.SWEEP_GRADIENT
            }
        }
        invalidate()
    }

    /**
     * 设置渐变方向，默认[GradientOrientation.LEFT_RIGHT]
     *
     * @param gradientOrientation 渐变方向：
     * [GradientOrientation.TOP_BOTTOM]，[GradientOrientation.RT_LB]，
     * [GradientOrientation.RIGHT_LEFT]，[GradientOrientation.RB_LT]，
     * [GradientOrientation.BOTTOM_TOP]，[GradientOrientation.LB_RT]，
     * [GradientOrientation.LEFT_RIGHT]，[GradientOrientation.LT_RB]
     */
    fun setGradientOrientation(gradientOrientation: GradientOrientation) {
        when (gradientOrientation) {
            GradientOrientation.TOP_BOTTOM -> {
                mGdn.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                mGdp.orientation = GradientDrawable.Orientation.TOP_BOTTOM
            }
            GradientOrientation.RT_LB -> {
                mGdn.orientation = GradientDrawable.Orientation.TR_BL
                mGdp.orientation = GradientDrawable.Orientation.TR_BL
            }
            GradientOrientation.RIGHT_LEFT -> {
                mGdn.orientation = GradientDrawable.Orientation.RIGHT_LEFT
                mGdp.orientation = GradientDrawable.Orientation.RIGHT_LEFT
            }
            GradientOrientation.RB_LT -> {
                mGdn.orientation = GradientDrawable.Orientation.BR_TL
                mGdp.orientation = GradientDrawable.Orientation.BR_TL
            }
            GradientOrientation.BOTTOM_TOP -> {
                mGdn.orientation = GradientDrawable.Orientation.BOTTOM_TOP
                mGdp.orientation = GradientDrawable.Orientation.BOTTOM_TOP
            }
            GradientOrientation.LB_RT -> {
                mGdn.orientation = GradientDrawable.Orientation.BL_TR
                mGdp.orientation = GradientDrawable.Orientation.BL_TR
            }
            GradientOrientation.LEFT_RIGHT -> {
                mGdn.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                mGdp.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            }
            GradientOrientation.LT_RB -> {
                mGdn.orientation = GradientDrawable.Orientation.TL_BR
                mGdp.orientation = GradientDrawable.Orientation.TL_BR
            }
        }
        invalidate()
    }

    /**
     * 设置是否使用level，默认false
     *
     * @param useLevel true、false
     */
    fun setUseLevel(useLevel: Boolean) {
        mGdn.useLevel = useLevel
        mGdp.useLevel = useLevel
        invalidate()
    }

    /**
     * 设置正常状态的填充色，使用渐变色时设置这个无效
     *
     * @param color color
     */
    fun setNormalSolid(color: String) {
        setNormalSolid(Color.parseColor(color))
    }

    /**
     * 设置正常状态的填充色，使用渐变色时设置这个无效
     *
     * @param color color
     */
    fun setNormalSolid(@ColorInt color: Int) {
        mGdn.setColor(color)
    }

    /**
     * 设置按下状态的填充色，使用渐变色时设置这个无效
     *
     * @param color color
     */
    fun setPressSolid(color: String) {
        setPressSolid(Color.parseColor(color))
    }

    /**
     * 设置按下状态的填充色，使用渐变色时设置这个无效
     *
     * @param color color
     */
    fun setPressSolid(@ColorInt color: Int) {
        mGdp.setColor(color)
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "performClick")
        return super.performClick()
    }
}