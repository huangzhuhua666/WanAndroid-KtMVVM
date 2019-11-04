package com.example.hzh.library.widget.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hzh.library.R
import com.example.hzh.library.databinding.DialogConfirmBinding
import kotlinx.android.synthetic.main.dialog_confirm.*
import kotlin.properties.Delegates

/**
 * Create by hzh on 2019/11/4.
 */
class ConfirmDialog private constructor(private val builder: Builder) : BaseDialog() {

    override val mLayoutId: Int
        get() = R.layout.dialog_confirm

    private var mBinding by Delegates.notNull<DialogConfirmBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, mLayoutId, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.builder = builder

        btnLeft.setOnClickListener {
            builder.leftClickListener?.invoke()
            dismiss()
        }

        btnRight.setOnClickListener {
            builder.rightClickListener?.invoke()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialogWindow?.setLayout(
            (dm.widthPixels * 0.8f).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    class Builder {

        var isSingleButton: Boolean = false

        var title: String = ""

        var titleColor: Int = Color.parseColor("#333333")

        var content: String = ""

        var contentColor: Int = Color.parseColor("#999999")

        var leftBtnText: String = ""

        var leftBtnColor: Int = Color.parseColor("#ff3b30")

        var leftClickListener: (() -> Unit)? = null

        var rightBtnText: String = ""

        var rightBtnColor: Int = Color.parseColor("#00a6ff")

        var rightClickListener: (() -> Unit)? = null

        fun build(): ConfirmDialog = ConfirmDialog(this)
    }
}