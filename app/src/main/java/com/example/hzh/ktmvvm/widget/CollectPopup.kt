package com.example.hzh.ktmvvm.widget

import android.content.Context
import android.view.View
import com.example.hzh.ktmvvm.R
import com.lxj.xpopup.core.AttachPopupView
import kotlinx.android.synthetic.main.popup_collect.view.*

/**
 * Create by hzh on 2019/11/7.
 */
@Suppress("ViewConstructor")
class CollectPopup(
    context: Context,
    private val onClick: ((View) -> Unit)
) : AttachPopupView(context) {

    override fun getImplLayoutId(): Int {
        return R.layout.popup_collect
    }

    override fun onCreate() {
        super.onCreate()
        btnArticle.setOnClickListener { v ->
            onClick.invoke(v)
            dismiss()
        }
        btnWebsite.setOnClickListener { v ->
            onClick.invoke(v)
            dismiss()
        }
    }
}