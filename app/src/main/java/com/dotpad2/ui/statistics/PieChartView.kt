package com.dotpad2.ui.statistics

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.dotpad2.R

class PieChartView(context: Context, attrs: AttributeSet) : BaseChartView(context, attrs) {

    val borderPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.colorPrimary)
        style = Paint.Style.STROKE
        strokeWidth = 4F
    }
    val chartPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            canvas.drawOval(chartArea, borderPaint)
            keepOvalDrawings(canvas)
            drawPieParts(canvas)
        }
    }

    private fun keepOvalDrawings(canvas: Canvas) {
        val clipPath = Path().apply {
            moveTo(chartArea.centerX(), chartArea.centerY())
            addOval(chartArea, Path.Direction.CW)
        }
        canvas.clipPath(clipPath)
    }

    private fun drawPieParts(canvas: Canvas) {
        var angleOffset = 0F
        var angles = mutableListOf<Float>()
        data?.forEach {
            chartPaint.color = it.second.value ?: Color.RED
            val arcTo = it.first * 3.6F

            canvas.drawArc(chartArea, angleOffset, arcTo, true, chartPaint)

            angleOffset += arcTo
            angles.add(arcTo)
        }

        drawPartLines(canvas, angles)
    }

    private fun drawPartLines(canvas: Canvas, angles: MutableList<Float>) {
        canvas.save()

        angles.forEach { angle ->
            val x = chartArea.centerX()
            val y = chartArea.centerY()
            canvas.rotate(angle + 180, x, y)
            canvas.drawLine(x, y, 0F, y, borderPaint)
        }

        canvas.restore()
    }

    private fun calculateChartRectange(): RectF {
        var xOffset = 0F
        var yOffset = 0F

        val size = when (width > height) {
            true -> {
                xOffset = (width - height) / 2F
                height
            }
            else -> {
                yOffset = (height - width) / 2F
                width
            }
        }.toFloat()

        val left = xOffset + paddingLeft
        val top = yOffset + paddingTop
        val right = size + xOffset - paddingRight
        val bottom = size + yOffset - paddingBottom

        return RectF(left, top, right, bottom)
    }

    private val chartArea by lazy { calculateChartRectange() }
}