package com.example.hzh.library.extension

import android.content.Context
import android.os.Build
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create by hzh on 2019/09/05.
 */
const val MM_dd = "M-d"
const val yyyy_M_d = "yyyy-M-d"
const val yyyy_MM_dd = "yyyy-MM-dd"

fun targetVersion(version: Int, after: () -> Unit = {}, before: () -> Unit = {}) {
    if (Build.VERSION.SDK_INT >= version) after()
    else before()
}

fun Char.digit(radix: Int): Int = Character.digit(this, radix)

fun Float.dp2px(context: Context): Int =
    (this * context.resources.displayMetrics.density + .5f).toInt()

fun Float.sp2px(context: Context): Int =
    (this * context.resources.displayMetrics.scaledDensity + .5f).toInt()

fun Float.px2dp(context: Context): Int =
    (this / context.resources.displayMetrics.density + .5f).toInt()

fun Float.px2sp(context: Context): Int =
    (this / context.resources.displayMetrics.scaledDensity + .5f).toInt()

fun Double.roundAndScale(scale: Int): Double =
    this.toBigDecimal().setScale(scale, RoundingMode.HALF_UP).toDouble()

fun Long.dateDistance(): String {
    val distance: String
    val current = System.currentTimeMillis()
    var diff = (current - this) / 1000
    distance = if (diff / 60 == 0L) {
        "刚刚"
    } else if (diff / 60 / 60 == 0L) {
        "${diff / 60}分钟前"
    } else if (diff / 60 / 60 / 24 == 0L) {
        "${diff / 60 / 60}小时前"
    } else if (diff / 60 / 60 / 24 / 2 == 0L) {
        "昨天"
    } else {
        diff = diff / 60 / 60 / 24
        if (diff <= 20) "${diff}天前" else this.formatDate(yyyy_M_d)
    }
    return distance
}

fun Long.formatDate(style: String): String =
    SimpleDateFormat(style, Locale.getDefault()).format(this)

fun String.hexStringToByteArray(): ByteArray {
    val len = length
    val data = ByteArray(len / 2)
    for (i in 0 until len step 2) {
        data[i / 2] = ((this[i].digit(16) shl 4) + this[i + 1].digit(16)).toByte()
    }
    return data
}

fun ByteArray.byteArrayToHexString(): String {
    val sb = StringBuilder(size * 2)
    forEach {
        val v = it.toInt() and 0xff
        if (v < 16) sb.append("0")
        sb.append(Integer.toHexString(v))
    }
    return sb.toString().toUpperCase(Locale.US)
}