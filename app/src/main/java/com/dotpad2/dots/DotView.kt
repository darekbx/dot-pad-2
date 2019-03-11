package com.dotpad2.dots

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.dotpad2.utils.TimeUtils

class DotView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        val SIZE_RATIO = 16
    }

    val dotPaint = Paint()
    val textPaint = Paint()
    var dotCreatedDate: Long = 0L
    var dotSize = 0
    var dotColor = 0
        set(value) {
            field = value
            dotPaint.color = value
        }

    init {
        dotPaint.apply { isAntiAlias = true }
        textPaint.apply {
            isAntiAlias = true
            color = Color.WHITE
            textSize = 16.0F
            textAlignment = TEXT_ALIGNMENT_CENTER
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            drawDotCircle(canvas)
            drawDotCreatedAgo(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(dotSize(), dotSize())
    }

    private fun drawDotCircle(canvas: Canvas) {
        val x = width / 2F
        val y = height / 2F
        val radius = dotSize() / 2F
        canvas.drawCircle(x, y, radius, dotPaint)
    }

    private fun drawDotCreatedAgo(canvas: Canvas) {
        var timeAgo = TimeUtils.calculateTimeAgo(dotCreatedDate)
        val (xOffset, yOffset) = calculateAgoOffset(timeAgo)
        val x = width / 2F
        val y = height / 2F
        canvas.drawText(timeAgo, x - xOffset, y + yOffset, textPaint)
    }

    private fun calculateAgoOffset(timeAgo: String): Pair<Float, Float> {
        val textBounds = Rect()
        textPaint.getTextBounds(timeAgo, 0, timeAgo.length, textBounds)
        val xOffset = textBounds.width() / 2F
        val yOffset = textBounds.height() / 2F
        return Pair(xOffset, yOffset)
    }

    private fun dotSize() = dotSize * SIZE_RATIO
}