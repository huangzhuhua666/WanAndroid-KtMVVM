package com.example.hzh.library.widget

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresPermission
import androidx.core.view.isGone
import com.example.hzh.library.R
import com.example.hzh.library.extension.inflate
import kotlinx.android.synthetic.main.layout_status.view.*

/**
 * Create by hzh on 2019/10/9.
 */
@Suppress("InflateParams")
class StatusLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mStatusView by lazy { context.inflate(R.layout.layout_status) }

    private var contentId = -1
    private var mContentView: View? = null

    var refreshAction: (() -> Unit)? = null

    init {
        mStatusView.isGone = true
        addView(mStatusView)

        btnRefresh.setOnClickListener {
            showContent()
            refreshAction?.invoke()
        }

        context.obtainStyledAttributes(attrs, R.styleable.StatusLayout)?.run {
            contentId = getResourceId(R.styleable.StatusLayout_content_view, -1)

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            getChildAt(i)?.let { if (it.id == contentId) mContentView = it }
        }
    }

    fun showContent() {
        mContentView?.let {
            if (it.isGone) {
                mStatusView.isGone = true
                it.isGone = false
            }
        }
    }

    fun showDataEmpty() {
        icon.setImageResource(R.mipmap.icon_hint_empty)
        tvTip.setText(R.string.no_data)

        if (mStatusView.isGone) {
            mStatusView.isGone = false
            mContentView?.isGone = true
        }
    }

    @Suppress("MissingPermission")
    fun showError() {
        if (isNetWorkAvailable()) {
            icon.setImageResource(R.mipmap.icon_hint_nerwork)
            tvTip.setText(R.string.net_error)
        } else {
            icon.setImageResource(R.mipmap.icon_hint_request)
            tvTip.setText(R.string.request_error)
        }

        if (mStatusView.isGone) {
            mStatusView.isGone = false
            mContentView?.isGone = true
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isNetWorkAvailable(): Boolean =
        (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo.let { it != null && it.isConnected }
}