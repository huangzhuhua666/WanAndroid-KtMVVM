package com.example.hzh.ktmvvm.widget.refreshlayout.painter

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

/**
 * @author huangzhuhua
 * @date 2022/1/10
 */
internal class ArrowPainter : Painter() {

    var arrowColor = Color.Black

    override val intrinsicSize: Size
        get() = Size.Unspecified

    private val mPath by lazy { Path() }

    override fun DrawScope.onDraw() {
        val width = size.width
        val height = size.height
        val lineWidth = width * 30 / 255
        mPath.reset()

        val vector1 = lineWidth * 0.70710678118654752440084436210485f
        val vector2 = lineWidth / 0.70710678118654752440084436210485f

        mPath.run {
            moveTo(center.x, height)
            lineTo(0f, center.y)
            lineTo(vector1, center.y - vector1)
            lineTo(center.x - lineWidth / 2f, height - vector2 - lineWidth / 2f)
            lineTo(center.x - lineWidth / 2f, 0f)
            lineTo(center.x + lineWidth / 2f, 0f)
            lineTo(center.x + lineWidth / 2f, height - vector2 - lineWidth / 2f)
            lineTo(width - vector1, center.y - vector1)
            lineTo(width, center.y)
            close()
        }

        drawPath(path = mPath, color = arrowColor)
    }
}