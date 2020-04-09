package com.example.hzh.library.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import com.example.hzh.library.extension.no
import com.example.hzh.library.extension.yes

/**
 * Create by hzh on 2019/07/04.
 */
class TagTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    companion object {
        private const val STATE_INSTANCE = "state_instance"
        private const val STATE_TAG_LAYOUT_ID = "state_tag_layout_id"
        private const val STATE_TAG_ID = "state_tag_id"
        private const val STATE_TAG = "state_tag"
        private const val STATE_CONTENT = "state_content"
    }

    @LayoutRes
    private var mTagLayoutId = -1

    @IdRes
    private var mTvTagId = -1

    private var mTag: String? = null
    private lateinit var mContent: String

    fun setTagAndContent(tag: String?, content: String) {
        mTag = tag
        mContent = content

        (tag != null && tag.trim() != "").yes {
            if (mTagLayoutId == -1 || mTvTagId == -1) throw RuntimeException("请先设置tag的布局id和tag TextView的id")

            val span = SpannableString("$tag$content")

            val view = LayoutInflater.from(context).inflate(mTagLayoutId, null)
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val tvTag = view.findViewById<TextView>(mTvTagId)
            tvTag.text = tag

            val bitmap = convertViewToBitmap(view)

            val drawable = BitmapDrawable(null, bitmap)
            drawable.setBounds(0, 0, tvTag.width, tvTag.height)

            val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)

            span.setSpan(imageSpan, 0, tag!!.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            text = span
        }.no { text = content }

        gravity = Gravity.CENTER_VERTICAL
    }

    private fun convertViewToBitmap(view: View): Bitmap {
        view.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun setTagLayoutId(@LayoutRes tagLayoutId: Int): TagTextView {
        mTagLayoutId = tagLayoutId
        return this
    }

    fun setTvTagId(@IdRes tvTagId: Int): TagTextView {
        mTvTagId = tvTagId
        return this
    }

    override fun onSaveInstanceState(): Parcelable? = bundleOf(
        STATE_INSTANCE to super.onSaveInstanceState(),
        STATE_TAG_LAYOUT_ID to mTagLayoutId,
        STATE_TAG_ID to mTvTagId,
        STATE_TAG to mTag,
        STATE_CONTENT to mContent
    )

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is Bundle) {
            super.onRestoreInstanceState(state)
            return
        }

        with(state) {
            super.onRestoreInstanceState(getParcelable(STATE_INSTANCE))

            mTagLayoutId = getInt(STATE_TAG_LAYOUT_ID, -1)
            mTvTagId = getInt(STATE_TAG_ID, -1)
            setTagAndContent(getString(STATE_TAG), getString(STATE_CONTENT) ?: "")
        }
    }
}