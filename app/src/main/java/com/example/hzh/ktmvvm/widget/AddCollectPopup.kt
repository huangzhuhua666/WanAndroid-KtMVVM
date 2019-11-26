package com.example.hzh.ktmvvm.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.example.hzh.ktmvvm.R
import com.example.hzh.library.extension.filterFastClickListener
import com.lxj.xpopup.core.AttachPopupView
import kotlinx.android.synthetic.main.popup_add_collect.view.*

/**
 * Create by hzh on 2019/11/7.
 */
@Suppress("ViewConstructor")
class AddCollectPopup(
    context: Context,
    private val action: ((View) -> Unit)
) : AttachPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.popup_add_collect

    override fun getPopupBackground(): Drawable = ColorDrawable(0x00000000)

    override fun onCreate() {
        super.onCreate()
        btnArticle.filterFastClickListener { v ->
            action.invoke(v)
            dismiss()
        }

        btnWebsite.filterFastClickListener { v ->
            action.invoke(v)
            dismiss()
        }
    }
}