package com.dotpad2.ui.dots

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DotView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        val SIZE_RATIO = 16
    }

    val dotPaint = Paint()
    var dotSize = 0
    var dotColor = 0

    init {
        dotPaint.apply {
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        dotPaint.color = dotColor

        if (dotPaint.color == 0) {
            dotPaint.color = Color.CYAN
        }

        val x = width / 2F
        val y = height / 2F
        val radius = dotSize() / 2F
        canvas?.drawCircle(x, y, radius, dotPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(dotSize(), dotSize())
    }

    private fun dotSize() = dotSize * SIZE_RATIO
}