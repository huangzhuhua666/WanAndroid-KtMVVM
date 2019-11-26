package com.example.hzh.ktmvvm.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.example.hzh.ktmvvm.R
import java.util.*

/**
 * Create by hzh on 2019/11/22.
 */
object TimePicker {

    private const val tenYear = 10 * 365 * 24 * 60 * 60 * 1000L

    fun getPicker(
        context: Context,
        title: String,
        listener: ((Date?, View?) -> Unit)
    ): TimePickerView =
        TimePickerBuilder(context) { date, v -> listener.invoke(date, v) }.run {
            val current = System.currentTimeMillis()
            val start = Calendar.getInstance().also { it.timeInMillis = current - tenYear }
            val end = Calendar.getInstance().also { it.timeInMillis = current + tenYear }

            setType(booleanArrayOf(true, true, true, false, false, false)) // 显示年、月、日
            setCancelText(context.getString(R.string.cancel)) // 取消文本
            setCancelColor(Color.WHITE) // 取消本文字体颜色
            setSubmitText(context.getString(R.string.confirm)) // 确认文本
            setSubmitColor(Color.WHITE) // 确认文本字体颜色
            setTitleText(title) // 标题
            setTitleColor(Color.WHITE) // 标题字体颜色
            setTitleBgColor(context.resources.getColor(R.color.appColor)) // 标题栏背景
            setBgColor(Color.WHITE) // 背景
            setOutSideCancelable(true) // 点击屏幕，点在控件外部范围时，是否取消显示
            isCyclic(false) // 是否循环滚动
            setDate(Calendar.getInstance()) // 默认当天
            setRangDate(start, end) // 起止年、月、日设定
            build()
        }
}