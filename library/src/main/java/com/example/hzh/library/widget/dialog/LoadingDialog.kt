package com.example.hzh.library.widget.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.example.hzh.library.R
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * Create by hzh on 2019/10/16.
 */
class LoadingDialog : BaseDialog() {

    override val mLayoutId: Int
        get() = R.layout.dialog_loading

    override val cancelable: Boolean
        get() = false

    override val canceledOnTouchOutside: Boolean
        get() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(tlv)
    }

    override fun onStart() {
        super.onStart()
        dialogWindow?.setLayout(
            (dm.widthPixels * 0.6f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}