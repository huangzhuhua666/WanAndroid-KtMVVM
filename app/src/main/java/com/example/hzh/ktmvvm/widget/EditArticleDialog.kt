package com.example.hzh.ktmvvm.widget

import androidx.core.view.isVisible
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.library.extension.addTextChangedListener
import com.example.hzh.library.extension.filterFastClickListener
import com.example.hzh.library.widget.dialog.BaseDialog
import kotlinx.android.synthetic.main.dialog_edit_article.*

/**
 * Create by hzh on 2019/11/9.
 */
class EditArticleDialog(private val action: ((Article) -> Unit)) : BaseDialog() {

    override val mLayoutId: Int
        get() = R.layout.dialog_edit_article


    override fun initView() {

    }

    override fun initListener() {
        etTitle.addTextChangedListener {
            afterTextChanged { btnCleanTitle.isVisible = it.toString().isNotBlank() }
        }

        etLink.addTextChangedListener {
            afterTextChanged { btnCleanLink.isVisible = it.toString().isNotBlank() }
        }

        etAuthor.addTextChangedListener {
            afterTextChanged { btnCleanAuthor.isVisible = it.toString().isNotBlank() }
        }

        btnCleanTitle.filterFastClickListener { etTitle.setText("") }

        btnCleanLink.filterFastClickListener { etLink.setText("") }

        btnCleanAuthor.filterFastClickListener { etAuthor.setText("") }

        btnCancel.filterFastClickListener { dismiss() }

        btnSubmit.filterFastClickListener {
            action.invoke(Article().apply {
                title = etTitle.text.trim().toString()
                link = etLink.text.trim().toString()
                author = etAuthor.text.trim().toString()
            })
        }
    }

    override fun initData() {

    }
}