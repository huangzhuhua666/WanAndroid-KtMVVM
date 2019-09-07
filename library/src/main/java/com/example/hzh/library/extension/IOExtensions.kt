package com.example.hzh.library.extension

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Create by hzh on 2019/09/06.
 */
fun InputStream.toByteArray(): ByteArray = ByteArrayOutputStream().let {
    try {
        it.write(this)
        it.close()
        it.toByteArray()
    } catch (e: IOException) {
        throw e
    }
}

fun OutputStream.write(inputStream: InputStream) {
    try {
        var len: Int
        val buffer = ByteArray(4096)
        while (inputStream.read(buffer).also { len = it } != -1) {
            write(buffer, 0, len)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}