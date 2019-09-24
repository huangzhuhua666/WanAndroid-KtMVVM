package com.example.hzh.library.widget

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.Bitmap.createBitmap
import android.graphics.Paint.*
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.example.hzh.library.R
import com.example.hzh.library.extension.dp2px
import com.example.hzh.library.extension.toBitmap
import kotlin.math.ceil
import kotlin.math.min

/**
 * Create by hzh on 2019/9/24.
 */
class TabView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mAlpha = 0.0f // 当前的透明度
    private var mPadding = 5 //文字和图片之间的距离5dp

    private var mText: String? = null // 描述文本
    private var mTextSize = 12 // 描述文本的默认字体大小12sp
    private var mTextColorNormal = 0x999999 // 描述文本的默认显示颜色
    private var mTextColorSelect = 0x46c01b // 述文本的默认选中显示颜色

    private var mIconNormal: Bitmap? = null // 默认图标
    private var mIconSelect: Bitmap? = null // 选中的图标

    private val mSelectPaint by lazy { Paint() } // 背景的画笔
    private val mIconAvailableRect by lazy { Rect() } // 图标可用的绘制区域
    private val mIconDrawRect by lazy { Rect() } // 图标真正的绘制区域

    private val mTextPaint by lazy { Paint(ANTI_ALIAS_FLAG) } // 描述文本的画笔
    private val mTextBound by lazy { Rect() } // 描述文本矩形测量大小
    private var mFmi: FontMetricsInt? = null // 用于获取字体的各种属性

    private var isShowRemove = false // 是否移除当前角标
    private var isShowPoint = false // 是否显示圆点
    private var mBadgeNumber = 0 // 角标数
    private var mBadgeBackgroundColor = 0xff0000 //默认红颜色

    init {
        mPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            mPadding.toFloat(),
            resources.displayMetrics
        ).toInt()

        mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            mTextSize.toFloat(),
            resources.displayMetrics
        ).toInt()

        context.obtainStyledAttributes(attrs, R.styleable.TabView).run {
            mText = getString(R.styleable.TabView_tabText)
            mTextSize = getDimensionPixelSize(R.styleable.TabView_tabTextSize, mTextSize)
            mTextColorNormal = getColor(R.styleable.TabView_tabTextColorNormal, mTextColorNormal)
            mTextColorSelect = getColor(R.styleable.TabView_tabTextColorSelect, mTextColorSelect)

            getDrawable(R.styleable.TabView_tabIconNormal)?.let {
                mIconNormal = if (it is BitmapDrawable) it.bitmap else it.toBitmap()
            }

            getDrawable(R.styleable.TabView_tabIconSelect)?.let {
                mIconSelect = if (it is BitmapDrawable) it.bitmap else it.toBitmap()
            }

            mBadgeBackgroundColor =
                getColor(R.styleable.TabView_badgeBackgroundColor, mBadgeBackgroundColor)

            mPadding = getDimensionPixelSize(R.styleable.TabView_paddingTextWithIcon, mPadding)

            recycle()
        }

        initText()
    }

    /**
     * 如果有设置文字就获取文字的区域大小
     */
    private fun initText() {
        mText?.run {
            mTextPaint.textSize = mTextSize.toFloat()
            mTextPaint.isDither = true
            mTextPaint.getTextBounds(mText, 0, mText!!.length, mTextBound)
            mFmi = mTextPaint.fontMetricsInt
        }
    }

    fun showPoint() {
        isShowRemove = false
        mBadgeNumber = -1
        isShowPoint = true

        invalidate()
    }

    fun showNumber(badgeNum: Int) {
        isShowRemove = false
        isShowPoint = false
        mBadgeNumber = badgeNum

        if (badgeNum > 0) invalidate()
        else {
            isShowRemove = true
            invalidate()
        }
    }

    fun removeShow() {
        mBadgeNumber = 0
        isShowPoint = false
        isShowRemove = true

        invalidate()
    }

    fun getBadgeNumber(): Int = mBadgeNumber

    fun isShowPoint(): Boolean = isShowPoint

    /**
     * @param alpha 对外提供的设置透明度的方法，取值 0.0 ~ 1.0
     */
    fun setIconAlpha(alpha: Float) {
        if (alpha in 0.0f..1.0f) {
            mAlpha = alpha
            invalidateView()
        } else throw IllegalArgumentException("透明度必须是 0.0 - 1.0")
    }

    /**
     * 根据当前所在线程更新界面
     */
    private fun invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) invalidate()
        else postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mText != null || (mIconNormal != null && mIconSelect != null)) {
            // 计算出可用绘图的区域
            val availableWidth = measuredWidth - paddingLeft - paddingRight
            var availableHeight = measuredHeight - paddingTop - paddingBottom

            if (mText != null && mIconNormal != null) {
                availableHeight -= mTextBound.height() + mPadding
                // 计算出图标可以绘制的画布大小
                mIconAvailableRect.set(
                    paddingLeft,
                    paddingTop,
                    paddingLeft + availableWidth,
                    paddingTop + availableHeight
                )

                // 计算文字的绘图区域
                val textLeft = paddingLeft + (availableWidth - mTextBound.width()) / 2
                val textTop = mIconAvailableRect.bottom + mPadding
                mTextBound.set(
                    textLeft,
                    textTop,
                    textLeft + mTextBound.width(),
                    textTop + mTextBound.height()
                )
            } else if (mText == null) {
                // 计算出图标可以绘制的画布大小
                mIconAvailableRect.set(
                    paddingLeft,
                    paddingTop,
                    paddingLeft + availableWidth,
                    paddingTop + availableHeight
                )
            } else if (mIconNormal == null) {
                // 计算文字的绘图区域
                val textLeft = paddingLeft + (availableWidth - mTextBound.width()) / 2
                val textTop = paddingTop + (availableHeight - mTextBound.height()) / 2
                mTextBound.set(
                    textLeft,
                    textTop,
                    textLeft + mTextBound.width(),
                    textTop + mTextBound.height()
                )
            }
        } else throw IllegalArgumentException("必须设置tabText或者tabIconSelected、tabIconNormal两个")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val alpha = ceil(mAlpha * 255.toDouble()).toInt()

        canvas?.run {
            if (mIconNormal != null && mIconSelect != null) {
                availableToDrawRect(mIconAvailableRect, mIconNormal!!)

                mSelectPaint.apply {
                    reset()
                    isAntiAlias = true // 设置抗锯齿
                    isFilterBitmap = true // 抗锯齿
                    setAlpha(255 - alpha)
                    drawBitmap(mIconNormal!!, null, mIconDrawRect, mSelectPaint)

                    reset()
                    isAntiAlias = true // 设置抗锯齿
                    isFilterBitmap = true // 抗锯齿
                    setAlpha(alpha)
                    drawBitmap(mIconSelect!!, null, mIconDrawRect, mSelectPaint)
                }
            }

            mText?.let {
                mTextPaint.apply {
                    // 绘制原始文字
                    color = mTextColorNormal
                    setAlpha(255 - alpha)
                    // 由于在该方法中，y轴坐标代表的是baseLine的值，mTextBound.height() + mFmi.bottom就是字体的高
                    // 所以在最后绘制前，修正偏移量，将文字向上修正mFmi.bottom / 2即可实现垂直居中
                    drawText(
                        it,
                        mTextBound.left.toFloat(),
                        mTextBound.bottom - mFmi!!.bottom / 2.toFloat(),
                        mTextPaint
                    )

                    // 绘制变色文字
                    color = mTextColorSelect
                    setAlpha(alpha)
                    drawText(
                        it,
                        mTextBound.left.toFloat(),
                        mTextBound.bottom - mFmi!!.bottom / 2.toFloat(),
                        mTextPaint
                    )
                }
            }

            //绘制角标
            if (!isShowRemove) drawBadge(this)
        }
    }

    private fun availableToDrawRect(availableRect: Rect, bitmap: Bitmap) {
        var dx = 0f
        var dy = 0f
        val wRatio = availableRect.width() * 1.0f / bitmap.width
        val hRatio = availableRect.height() * 1.0f / bitmap.height

        if (wRatio > hRatio) dx = (availableRect.width() - hRatio * bitmap.width) / 2
        else dy = (availableRect.height() - wRatio * bitmap.height) / 2

        val left = (availableRect.left + dx + .5f).toInt()
        val top = (availableRect.top + dy + .5f).toInt()
        val right = (availableRect.right - dx + .5f).toInt()
        val bottom = (availableRect.bottom - dy + .5f).toInt()
        mIconDrawRect.set(left, top, right, bottom)
    }

    private fun drawBadge(c: Canvas) {
        var i = min(measuredWidth / 14, measuredHeight / 9)

        if (mBadgeNumber > 0) {
            val bgPaint = Paint(ANTI_ALIAS_FLAG).apply { color = mBadgeBackgroundColor }
            val number = if (mBadgeNumber > 99) "99+" else mBadgeNumber.toString()
            val textSize = if (i / 1.5f == 0f) 5.0f else i / 1.5f

            val width: Int
            val height = i.toFloat().dp2px(context)
            val bitmap: Bitmap

            bitmap = when (number.length) {
                1 -> {
                    width = i.toFloat().dp2px(context)
                    createBitmap(width, height, Config.ARGB_8888)
                }
                2 -> {
                    width = (i + 5).toFloat().dp2px(context)
                    createBitmap(width, height, Config.ARGB_8888)
                }
                else -> {
                    width = (i + 8).toFloat().dp2px(context)
                    createBitmap(width, height, Config.ARGB_8888)
                }
            }

            val canvasMessages = Canvas(bitmap)
            val messageRectF = RectF(0.0f, 0.0f, width.toFloat(), height.toFloat())
            canvasMessages.drawRoundRect(messageRectF, 50f, 50f, bgPaint) // 画椭圆

            val numberPaint = Paint(ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                this.textSize = textSize.dp2px(context).toFloat()
                textAlign = Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }
            val fontMetrics = numberPaint.fontMetrics
            val x = width / 2.0f
            val y = fontMetrics.let { height / 2.0f - it.descent + (it.descent - it.ascent) / 2 }
            canvasMessages.drawText(number, x, y, numberPaint)

            val left = measuredWidth / 10 * 6.0f
            val top = 5.0f.dp2px(context).toFloat()
            c.drawBitmap(bitmap, left, top, null)
            bitmap.recycle()
        } else {
            if (isShowPoint) {
                val paint = Paint(ANTI_ALIAS_FLAG).apply { color = mBadgeBackgroundColor }
                val left = measuredWidth / 10 * 6.0f
                val top = 5.0f.dp2px(context).toFloat()
                i = if (i > 10) 10 else i

                val width = i.toFloat().dp2px(context)
                val messageRectF =
                    RectF(left, top, left + width, top + width)
                c.drawOval(messageRectF, paint)
            }
        }
    }
}