package com.example.hzh.ktmvvm.widget.refreshlayout.painter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.rotate
import kotlin.math.max
import kotlin.math.min

/**
 * @author huangzhuhua
 * @date 2022/1/10
 */
internal class ProgressPainter : Painter() {

    var progressColor = Color.Black

    override val intrinsicSize: Size
        get() = Size.Unspecified

    private val mPath by lazy { Path() }

    var progressDegree by mutableStateOf(0f)

    override fun DrawScope.onDraw() {
        val width = size.width
        val radius = max(1f, width / 22f)

        mPath.run {
            reset()
            addOval(Rect(center = Offset(width - radius, center.y), radius = radius))

            val rect = Rect(
                left = width - 5 * radius,
                top = center.y - radius,
                right = width - radius,
                bottom = center.y + radius
            )
            addRect(rect)

            addOval(Rect(center = Offset(width - 5 * radius, center.y), radius = radius))
        }

        drawIntoCanvas {
            it.save()
            it.rotate(progressDegree, pivotX = center.x, pivotY = center.y)

            for (i in 0 until 12) {
                it.rotate(30f, pivotX = center.x, pivotY = center.y)

                val alpha = min(1f, ((i + 5) * 0x11) / 255f)
                drawPath(path = mPath, color = progressColor, alpha = alpha)
            }

            it.restore()
        }
    }
}