package com.example.hzh.ktmvvm.widget.refreshlayout.painter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * @author huangzhuhua
 * @date 2022/1/13
 */
internal class CircularProgressPainter : Painter() {

    var arcRadius by mutableStateOf(0.dp)
    var strokeWidth by mutableStateOf(5.dp)

    var arrowEnabled by mutableStateOf(false)
    var arrowWidth by mutableStateOf(0.dp)
    var arrowHeight by mutableStateOf(0.dp)
    var arrowScale by mutableStateOf(1f)

    var color by mutableStateOf(Color.Unspecified)
    var alpha by mutableStateOf(1f)
    var startTrim by mutableStateOf(0f)
    var endTrim by mutableStateOf(0f)
    var rotation by mutableStateOf(0f)

    private val mArrowPath by lazy {
        Path().apply { fillType = PathFillType.EvenOdd }
    }

    override val intrinsicSize: Size
        get() = Size.Unspecified

    override fun DrawScope.onDraw() {
        rotate(degrees = rotation) {
            val arcRadius = arcRadius.toPx() + strokeWidth.toPx() / 2f
            val arcBounds = Rect(
                center.x - arcRadius,
                center.y - arcRadius,
                center.x + arcRadius,
                center.y + arcRadius
            )

            val startAngle = (startTrim + rotation) * 360
            val endAngle = (endTrim + rotation) * 360
            val sweepAngle = endAngle - startAngle

            drawArc(
                color = color,
                alpha = alpha,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = arcBounds.topLeft,
                size = arcBounds.size,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Square
                )
            )

            if (arrowEnabled) {
                drawArrow(startAngle = startAngle, sweepAngle = sweepAngle, bounds = arcBounds)
            }
        }
    }

    private fun DrawScope.drawArrow(startAngle: Float, sweepAngle: Float, bounds: Rect) {
        mArrowPath.run {
            reset()
            moveTo(0f, 0f)
            lineTo(x = arrowWidth.toPx() * arrowScale, y = 0f)
            lineTo(x = arrowWidth.toPx() * arrowScale / 2, y = arrowHeight.toPx() * arrowScale)

            val radius = min(bounds.width, bounds.height) / 2f
            val inset = arrowWidth.toPx() * arrowScale / 2f
            translate(
                Offset(
                    x = radius + bounds.center.x - inset,
                    y = bounds.center.y + strokeWidth.toPx() / 2f
                )
            )
            close()

            rotate(degrees = startAngle + sweepAngle) {
                drawPath(
                    path = mArrowPath,
                    color = color,
                    alpha = alpha
                )
            }
        }
    }

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }
}