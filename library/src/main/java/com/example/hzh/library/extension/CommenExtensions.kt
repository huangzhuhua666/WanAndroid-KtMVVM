package com.example.hzh.library.extension

import android.content.Context
import java.util.*

/**
 * Create by hzh on 2019/09/05.
 */
fun Char.digit(radix: Int): Int = Character.digit(this, radix)

fun Float.dp2px(context: Context) = (this * context.resources.displayMetrics.density + .5f).toInt()

fun Float.sp2px(context: Context) = (this * context.resources.displayMetrics.scaledDensity + .5f).toInt()

fun Float.px2dp(context: Context) = (this / context.resources.displayMetrics.density + .5f).toInt()

fun Float.px2sp(context: Context) = (this / context.resources.displayMetrics.scaledDensity + .5f).toInt()

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