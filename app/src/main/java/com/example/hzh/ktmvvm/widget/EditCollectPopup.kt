package com.example.hzh.ktmvvm.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.example.hzh.ktmvvm.R
import com.lxj.xpopup.core.AttachPopupView
import kotlinx.android.synthetic.main.popup_edit_collect.view.*

/**
 * Create by hzh on 2019/11/8.
 */
@Suppress("ViewConstructor")
class EditCollectPopup(
    context: Context,
    private val action: ((View) -> Unit)
) : AttachPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.popup_edit_collect

    override fun getPopupBackground(): Drawable = ColorDrawable(0x00000000)

    override fun onCreate() {
        super.onCreate()
        btnEdit.setOnClickListener {
            action.invoke(it)
            dismiss()
        }

        btnDelete.setOnClickListener {
            action.invoke(it)
            dismiss()
        }
    }
}