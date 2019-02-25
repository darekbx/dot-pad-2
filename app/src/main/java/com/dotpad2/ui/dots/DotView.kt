package com.dotpad2.ui.dots

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.dotpad2.model.Dot

class DotView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        val SIZE_RATIO = 16
    }

    val dotPaint = Paint()

    init {
        dotPaint.apply {
            color = dot.color
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val x = width / 2F
        val y = height / 2F
        val radius = dot.size / 2F
        canvas?.drawCircle(x, y, radius, dotPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(dotSize(), dotSize())
    }

    private val dot by lazy { tag as Dot }
    private fun dotSize() = dot.size * SIZE_RATIO
}