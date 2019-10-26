package com.example.hzh.library.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * Create by hzh on 2019/9/24.
 */
fun Drawable.toBitmap(): Bitmap? {
    return try {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888).also {
            Canvas(it).let { c ->
                setBounds(0, 0, c.width, c.height)
                draw(c)
            }
        }
    } catch (e: Exception) {
        null
    }
}