package com.example.hzh.library.widget.dialog

import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import com.example.hzh.library.R
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * Create by hzh on 2019/10/16.
 */
@Suppress("InflateParams")
class LoadingDialog : DialogFragment() {

    private var mShowTag = ""
    private var mLastTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_loading, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(tlv)
    }

    override fun onStart() {
        super.onStart()
        dialog?.run {
            val dm = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)
            window?.let {
                it.setLayout((dm.widthPixels * 0.6f).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
                it.setBackgroundDrawableResource(R.drawable.bg_dialog_white_radius_10)
                it.attributes.windowAnimations = R.style.AnimDialog
            }

            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    fun show(fragment: Fragment) {
        show(fragment.fragmentManager!!, fragment.javaClass.name)
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, activity.javaClass.name)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isRepeatedShow(tag ?: "")) super.show(manager, tag)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return if (!isRepeatedShow(tag ?: "")) super.show(transaction, tag) else -1
    }

    private fun isRepeatedShow(tag: String): Boolean {
        val result = tag == mShowTag && SystemClock.uptimeMillis() - mLastTime < 500
        mShowTag = tag
        mLastTime = SystemClock.uptimeMillis()
        return result
    }

    fun isShowing(): Boolean = dialog?.isShowing ?: false
}