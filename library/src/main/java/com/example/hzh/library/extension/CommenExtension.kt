package com.example.hzh.library.extension

import java.util.*

/**
 * Create by hzh on 2019/09/05.
 */
fun Char.digit(radix: Int): Int = Character.digit(this, radix)

fun ByteArray.byteArrayToHexString(): String {
    val sb = StringBuilder(size * 2)
    forEach {
        val v = it.toInt() and 0xff
        if (v < 16) sb.append("0")
        sb.append(Integer.toHexString(v))
    }
    return sb.toString().toUpperCase(Locale.US)
}

fun String.hexStringToByteArray(): ByteArray {
    val len = length
    val data = ByteArray(len / 2)
    for (i in 0 until len step 2) {
        data[i / 2] = ((this[i].digit(16) shl 4) + this[i + 1].digit(16)).toByte()
    }
    return data
}