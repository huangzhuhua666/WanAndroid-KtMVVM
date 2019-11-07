package com.example.hzh.library.widget.dialog

import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.*
import com.example.hzh.library.R

/**
 * Create by hzh on 2019/11/4.
 */
abstract class BaseDialog : DialogFragment() {

    protected abstract val mLayoutId: Int

    protected open val cancelable: Boolean
        get() = true

    protected open val canceledOnTouchOutside: Boolean
        get() = true

    protected open val backgroundDrawableResource: Int
        get() = R.drawable.bg_dialog_white_radius_10

    protected open val windowAnimations: Int
        get() = R.style.AnimDialog

    protected open val gravity: Int
        get() = Gravity.CENTER

    private var mShowTag = ""
    private val mills = LongArray(2)
    protected val dm by lazy { DisplayMetrics() }
    protected var dialogWindow: Window? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(mLayoutId, null, false)

    override fun onStart() {
        super.onStart()
        dialog?.run {
            activity?.windowManager?.defaultDisplay?.getMetrics(dm)

            dialogWindow = window?.let {
                it.setGravity(gravity)
                it.setBackgroundDrawableResource(backgroundDrawableResource)
                it.attributes.windowAnimations = windowAnimations
                it
            }

            setCancelable(cancelable)
            setCanceledOnTouchOutside(canceledOnTouchOutside)
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
        System.arraycopy(mills, 1, mills, 0, 1)
        mills[1] = SystemClock.uptimeMillis()

        val result = tag == mShowTag && mills[1] - mills[0] < 500
        mShowTag = tag
        return result
    }

    fun isShowing(): Boolean = dialog?.isShowing ?: false
}