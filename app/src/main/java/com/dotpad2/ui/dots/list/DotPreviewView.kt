package com.dotpad2.ui.dots.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DotPreviewView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    val dotPaint = Paint()
    var dotColor = 0
        set(value) {
            field = value
            dotPaint.color = value
        }

    init {
        dotPaint.apply { isAntiAlias = true }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            drawDotCircle(canvas)
        }
    }

    private fun drawDotCircle(canvas: Canvas) {
        val y = height / 2F
        val radius = y
        canvas.drawCircle(y, y, radius, dotPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(heightSize, heightSize)
    }
}