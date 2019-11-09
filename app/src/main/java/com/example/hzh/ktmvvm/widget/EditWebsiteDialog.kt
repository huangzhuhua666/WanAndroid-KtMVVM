package com.example.hzh.ktmvvm.widget

import androidx.core.view.isVisible
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.library.extension.addTextChangedListener
import com.example.hzh.library.widget.dialog.BaseDialog
import kotlinx.android.synthetic.main.dialog_edit_website.*

/**
 * Create by hzh on 2019/11/8.
 */
class EditWebsiteDialog(
    private val website: Website = Website(),
    private val action: ((Website) -> Unit)
) : BaseDialog() {

    override val mLayoutId: Int
        get() = R.layout.dialog_edit_website


    override fun initView() {

    }

    override fun initListener() {
        etName.addTextChangedListener {
            afterTextChanged { btnCleanName.isVisible = it.toString().isNotBlank() }
        }

        etLink.addTextChangedListener {
            afterTextChanged { btnCleanLink.isVisible = it.toString().isNotBlank() }
        }

        btnCleanName.setOnClickListener { etName.setText("") }

        btnCleanLink.setOnClickListener { etLink.setText("") }

        btnCancel.setOnClickListener { dismiss() }

        btnSubmit.setOnClickListener {
            action.invoke(Website().apply {
                websiteId = website.websiteId
                name = etName.text.trim().toString()
                link = etLink.text.trim().toString()
            })
        }
    }

    override fun initData() {
        website.let {
            etName.setText(it.name)
            etLink.setText(it.link)
        }
    }
}