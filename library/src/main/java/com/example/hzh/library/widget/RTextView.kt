package com.example.hzh.library.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.Log
import com.example.hzh.library.R

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

        /**
         * 线性渐变（默认）
         */
        const val LINEAR = 0

        /**
         * 放射状渐变
         */
        const val RADIAL = 1

        /**
         * 扫描渐变
         */
        const val SWEEP = 2

        /**
         * 渐变方向从上到下
         */
        const val TOP_BOTTOM = 0

        /**
         * 渐变方向从右上到左下
         */
        const val RT_LB = 1

        /**
         * 渐变方向从右到左
         */
        const val RIGHT_LEFT = 2

        /**
         * 渐变方向从右下到左上
         */
        const val RB_LT = 3

        /**
         * 渐变方向从下到上
         */
        const val BOTTOM_TOP = 4

        /**
         * 渐变方向从左下到右上
         */
        const val LB_RT = 5

        /**
         * 渐变方向从左到右（默认）
         */
        const val LEFT_RIGHT = 6

        /**
         * 渐变方向从左上到右下
         */
        const val LT_RB = 7
    }

    @IntDef(LINEAR, RADIAL, SWEEP)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class GradientType

    @IntDef(TOP_BOTTOM, RT_LB, RIGHT_LEFT, RB_LT, BOTTOM_TOP, LB_RT, LEFT_RIGHT, LT_RB)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class GradientOrientation

    private val mGdn: GradientDrawable
    private val mGdp: GradientDrawable

    private var mLeftTop: Float = .0f
    private var mLeftBottom: Float = .0f
    private var mRightTop: Float = .0f
    private var mRightBottom: Float = .0f

    @ColorInt
    private var mNormalTextColor: Int = 0
    @ColorInt
    private var mPressTextColor: Int = 0

    init {
        mGdn = GradientDrawable()
        mGdp = GradientDrawable()
        val bg = StateListDrawable()
        bg.addState(intArrayOf(android.R.attr.state_pressed), mGdp)
        bg.addState(intArrayOf(), mGdn)
        background = bg
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RTextView)

        // 圆角
        val radii = ta.getDimension(R.styleable.RTextView_radii, -1.0f)
        if (radii > -1.0f) setRadii(radii)

        // 左上
        val leftTop = ta.getDimension(R.styleable.RTextView_lefTopRadius, -1.0f)
        if (leftTop > -1.0f) setLeftTopRadius(leftTop)
        // 左下
        val leftBottom = ta.getDimension(R.styleable.RTextView_leftBottomRadius, -1.0f)
        if (leftBottom > -1.0f) setLeftBottomRadius(leftBottom)
        // 右上
        val rightTop = ta.getDimension(R.styleable.RTextView_rightTopRadius, -1.0f)
        if (rightTop > -1.0f) setRightTopRadius(rightTop)
        // 右下
        val rightBottom = ta.getDimension(R.styleable.RTextView_rightBottomRadius, -1.0f)
        if (rightBottom > -1.0f) setRightBottomRadius(rightBottom)

        // 字体颜色
        mNormalTextColor = ta.getColor(R.styleable.RTextView_normalTextColor, currentTextColor)
        mPressTextColor = ta.getColor(R.styleable.RTextView_pressTextColor, mNormalTextColor)
        setTextStateListColor()

        // 正常状态的边框
        val normalStrokeWidth = ta.getDimension(R.styleable.RTextView_normalStrokeWidth, .0f)
        val normalStrokeColor = ta.getColor(R.styleable.RTextView_normalStrokeColor, 0)
        setNormalStroke((normalStrokeWidth + .5f).toInt(), normalStrokeColor)

        // 按下状态的边框
        val pressStrokeWidth =
            ta.getDimension(R.styleable.RTextView_pressStrokeWidth, normalStrokeWidth)
        val pressStrokeColor =
            ta.getColor(R.styleable.RTextView_pressStrokeColor, normalStrokeColor)
        setPressStroke((pressStrokeWidth + .5f).toInt(), pressStrokeColor)

        val isGradientSolid = ta.getBoolean(R.styleable.RTextView_isGradientSolid, false)
        if (isGradientSolid) {// 使用渐变色
            // 正常状态的渐变色
            val normalGradientStart = ta.getColor(
                R.styleable.RTextView_normalGradientStart,
                0
            )
            val normalGradientEnd = ta.getColor(
                R.styleable.RTextView_normalGradientEnd,
                normalGradientStart
            )
            setNormalGradient(normalGradientStart, normalGradientEnd)

            // 按下状态的渐变色
            val pressGradientStart = ta.getColor(
                R.styleable.RTextView_pressGradientStart,
                normalGradientStart
            )
            val pressGradientEnd = ta.getColor(
                R.styleable.RTextView_pressGradientEnd,
                pressGradientStart
            )
            setPresGradient(pressGradientStart, pressGradientEnd)

            // 渐变色的类型
            val gradientType = when (ta.getInt(R.styleable.RTextView_gradientType, 0)) {
                0 -> LINEAR
                1 -> RADIAL
                2 -> SWEEP
                else -> LINEAR
            }
            setGradientType(gradientType)

            // 渐变色的方向
            val gradientOrientation = when (ta.getInt(
                R.styleable.RTextView_gradientOrientation,
                6
            )) {
                0 -> TOP_BOTTOM
                1 -> RT_LB
                2 -> RIGHT_LEFT
                3 -> RB_LT
                4 -> BOTTOM_TOP
                5 -> LB_RT
                6 -> LEFT_RIGHT
                7 -> LT_RB
                else -> LEFT_RIGHT
            }
            setGradientOrientation(gradientOrientation)

            val useLevel = ta.getBoolean(R.styleable.RTextView_useLevel, false)
            setUseLevel(useLevel)
        } else {
            // 不使用渐变色
            // 正常状态的填充色
            val normalSolid = ta.getColor(R.styleable.RTextView_normalSolid, 0)
            setNormalSolid(normalSolid)

            // 按下状态的填充色
            val pressSolid = ta.getColor(R.styleable.RTextView_pressSolid, normalSolid)
            setPressSolid(pressSolid)
        }

        ta.recycle()
    }

    /**
     * 设置四个角的圆角
     *
     * @param radii 圆角大小
     */
    fun setRadii(radii: Float) {
        mLeftTop = radii
        mLeftBottom = mLeftTop
        mRightTop = mLeftTop
        mRightBottom = mLeftTop
        resetRadius()
    }

    /**
     * 设置左上角的圆角
     *
     * @param leftTop 圆角大小
     */
    fun setLeftTopRadius(leftTop: Float) {
        mLeftTop = leftTop
        resetRadius()
    }

    /**
     * 设置左下角的圆角
     *
     * @param leftBottom 圆角大小
     */
    fun setLeftBottomRadius(leftBottom: Float) {
        mLeftBottom = leftBottom
        resetRadius()
    }

    /**
     * 设置右上角的圆角
     *
     * @param rightTop 圆角大小
     */
    fun setRightTopRadius(rightTop: Float) {
        mRightTop = rightTop
        resetRadius()
    }

    /**
     * 设置右下角的圆角
     *
     * @param rightBottom 圆角大小
     */
    fun setRightBottomRadius(rightBottom: Float) {
        mRightBottom = rightBottom
        resetRadius()
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
            mLeftTop, mLeftTop, mRightTop, mRightTop,
            mRightBottom, mRightBottom, mLeftBottom, mLeftBottom
        )
        mGdn.cornerRadii = radii
        mGdp.cornerRadii = radii
    }

    /**
     * 设置正常状态的文字颜色
     *
     * @param color color
     */
    fun setNormalTextColor(@ColorInt color: Int) {
        mNormalTextColor = color
        setTextStateListColor()
    }

    /**
     * 设置正常状态的文字颜色
     *
     * @param color color
     */
    fun setNormalTextColor(color: String) {
        mNormalTextColor = Color.parseColor(color)
        setTextStateListColor()
    }

    /**
     * 设置按下状态的文字颜色
     *
     * @param color color
     */
    fun setPressTextColor(@ColorInt color: Int) {
        mPressTextColor = color
        setTextStateListColor()
    }

    /**
     * 设置按下状态的文字颜色
     *
     * @param color color
     */
    fun setPressTextColor(color: String) {
        mPressTextColor = Color.parseColor(color)
        setTextStateListColor()
    }

    /**
     * 更新文字的颜色
     */
    private fun setTextStateListColor() {
        val state = arrayOfNulls<IntArray>(2)
        state[0] = intArrayOf(android.R.attr.state_pressed)
        state[1] = intArrayOf()
        setTextColor(ColorStateList(state, intArrayOf(mPressTextColor, mNormalTextColor)))
    }

    /**
     * 设置正常状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setNormalStroke(width: Int, @ColorInt color: Int) {
        mGdn.setStroke(width, color)
    }

    /**
     * 设置正常状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setNormalStroke(width: Int, color: String) {
        mGdn.setStroke(width, Color.parseColor(color))
    }

    /**
     * 设置按下状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setPressStroke(width: Int, @ColorInt color: Int) {
        mGdp.setStroke(width, color)
    }

    /**
     * 设置按下状态的边框
     *
     * @param width 边框线条大小
     * @param color 边框颜色
     */
    fun setPressStroke(width: Int, color: String) {
        mGdp.setStroke(width, Color.parseColor(color))
    }

    /**
     * 设置正常状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setNormalGradient(@ColorInt start: Int, @ColorInt end: Int) {
        mGdn.colors = intArrayOf(start, end)
    }

    /**
     * 设置正常状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setNormalGradient(start: String, end: String) {
        mGdn.colors = intArrayOf(Color.parseColor(start), Color.parseColor(end))
    }

    /**
     * 设置按下状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setPresGradient(@ColorInt start: Int, @ColorInt end: Int) {
        mGdp.colors = intArrayOf(start, end)
    }

    /**
     * 设置按下状态的渐变色
     *
     * @param start 开始色
     * @param end   结束色
     */
    fun setPressGradient(start: String, end: String) {
        mGdp.colors = intArrayOf(Color.parseColor(start), Color.parseColor(end))
    }

    /**
     * 设置渐变的类型，默认[LINEAR]
     *
     * @param gradientType 渐变的类型：[LINEAR]，[RADIAL]，[SWEEP]
     */
    fun setGradientType(@GradientType gradientType: Int) {
        when (gradientType) {
            LINEAR -> {
                mGdn.gradientType = GradientDrawable.LINEAR_GRADIENT
                mGdp.gradientType = GradientDrawable.LINEAR_GRADIENT
            }
            RADIAL -> {
                mGdn.gradientType = GradientDrawable.RADIAL_GRADIENT
                mGdp.gradientType = GradientDrawable.RADIAL_GRADIENT
            }
            SWEEP -> {
                mGdn.gradientType = GradientDrawable.SWEEP_GRADIENT
                mGdp.gradientType = GradientDrawable.SWEEP_GRADIENT
            }
            else -> {
                mGdn.gradientType = GradientDrawable.LINEAR_GRADIENT
                mGdp.gradientType = GradientDrawable.LINEAR_GRADIENT
            }
        }
    }

    /**
     * 设置渐变方向，默认[LEFT_RIGHT]
     *
     * @param gradientOrientation 渐变方向：[TOP_BOTTOM]，[RT_LB]，
     * [RIGHT_LEFT]，[RB_LT]，
     * [BOTTOM_TOP]，[LB_RT]，
     * [LEFT_RIGHT]，[LT_RB]
     */
    fun setGradientOrientation(@GradientOrientation gradientOrientation: Int) {
        when (gradientOrientation) {
            TOP_BOTTOM -> {
                mGdn.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                mGdp.orientation = GradientDrawable.Orientation.TOP_BOTTOM
            }
            RT_LB -> {
                mGdn.orientation = GradientDrawable.Orientation.TR_BL
                mGdp.orientation = GradientDrawable.Orientation.TR_BL
            }
            RIGHT_LEFT -> {
                mGdn.orientation = GradientDrawable.Orientation.RIGHT_LEFT
                mGdp.orientation = GradientDrawable.Orientation.RIGHT_LEFT
            }
            RB_LT -> {
                mGdn.orientation = GradientDrawable.Orientation.BR_TL
                mGdp.orientation = GradientDrawable.Orientation.BR_TL
            }
            BOTTOM_TOP -> {
                mGdn.orientation = GradientDrawable.Orientation.BOTTOM_TOP
                mGdp.orientation = GradientDrawable.Orientation.BOTTOM_TOP
            }
            LB_RT -> {
                mGdn.orientation = GradientDrawable.Orientation.BL_TR
                mGdp.orientation = GradientDrawable.Orientation.BL_TR
            }
            LEFT_RIGHT -> {
                mGdn.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                mGdp.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            }
            LT_RB -> {
                mGdn.orientation = GradientDrawable.Orientation.TL_BR
                mGdp.orientation = GradientDrawable.Orientation.TL_BR
            }
            else -> {
                mGdn.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                mGdp.orientation = GradientDrawable.Orientation.LEFT_RIGHT
            }
        }
    }

    /**
     * 设置是否使用level，默认false
     *
     * @param useLevel true、false
     */
    fun setUseLevel(useLevel: Boolean) {
        mGdn.useLevel = useLevel
        mGdp.useLevel = useLevel
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
     * 设置正常状态的填充色，使用渐变色时设置这个无效
     *
     * @param color color
     */
    fun setNormalSolid(color: String) {
        mGdn.setColor(Color.parseColor(color))
    }

    /**
     * 设置按下状态的填充色，使用渐变色时设置这个无效
     *
     * @param color color
     */
    fun setPressSolid(@ColorInt color: Int) {
        mGdp.setColor(color)
    }

    /**
     * 设置按下状态的填充色，使用渐变色时设置这个无效
     *
     * @param color color
     */
    fun setPressSolid(color: String) {
        mGdp.setColor(Color.parseColor(color))
    }

    override fun performClick(): Boolean {
        Log.d(TAG, "performClick")
        return super.performClick()
    }
}