package com.example.hzh.library.extension

import java.io.*

/**
 * Create by hzh on 2019/09/06.
 */
fun InputStream.toByteArray(): ByteArray = ByteArrayOutputStream().let {
    try {
        it.write(this, 4096)
        it.close()
        it.toByteArray()
    } catch (e: IOException) {
        closeSave()
        throw e
    }
}

fun OutputStream.write(inputStream: InputStream, size: Int = 1024, block: (Int) -> Unit = {}) =
    try {
        var len: Int
        val buffer = ByteArray(size)
        while (inputStream.read(buffer).also { len = it } != -1) {
            write(buffer, 0, len)
            flush()
            block(len)
        }
    } catch (e: IOException) {
        closeSave()
        throw e
    }

fun Closeable.closeSave() = try {
    close()
} catch (e: Exception) {
    e.printStackTrace()
}
