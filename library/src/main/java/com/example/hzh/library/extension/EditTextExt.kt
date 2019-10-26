package com.example.hzh.library.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Create by hzh on 2019/08/09.
 */
fun EditText.addTextChangedListener(init: TextChangeHelper.() -> Unit) {
    val listener = TextChangeHelper()
    listener.init()
    addTextChangedListener(listener)
}

private typealias TextChanging = (CharSequence?, Int, Int, Int) -> Unit
private typealias AfterTextChanged = (Editable?) -> Unit

class TextChangeHelper : TextWatcher {

    private var beforeTextChanged: TextChanging? = null
    private var textChanged: TextChanging? = null
    private var afterTextChanged: AfterTextChanged? = null

    fun beforeTextChanged(onBeforeTextChanged: TextChanging) {
        beforeTextChanged = onBeforeTextChanged
    }

    fun onTextChanged(onTextChanged: TextChanging) {
        textChanged = onTextChanged
    }

    fun afterTextChanged(onAfterTextChanged: AfterTextChanged) {
        afterTextChanged = onAfterTextChanged
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s)
    }
}