package com.dotpad2.ui.dots

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.dotpad2.utils.TimeUtils
import android.view.MotionEvent
import com.dotpad2.R
import java.util.*

class DotView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        val SIZE_RATIO = 16
        val REMINDER_INNER_PADDING = 6F
    }

    private val dotPaint = Paint()
    private val textPaint = Paint()
    private val reminderPaint = Paint()
    private var dragPaint = Paint()

    var isDragDropEnabled = false
        set(value) {
            field = value
            invalidate()
        }

    var dotIsSticked: Boolean = false
    var dotCreatedDate: Long = 0L
    var dotSize = 0
    var dotReminder: Long? = 0L
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
        reminderPaint.apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 5F
        }
        dragPaint.apply {
            isAntiAlias = true
            color = context.getColor(R.color.colorPrimary)
            style = Paint.Style.STROKE
            strokeWidth = DotBoardView.DRAG_PADDING
            pathEffect = setPathEffect(DashPathEffect(floatArrayOf(40f, 40f), 20f))
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isDragDropEnabled) {
            event?.let { makeDragDrop(it, event) }
        }

        return super.onTouchEvent(event)
    }

    private fun makeDragDrop(it: MotionEvent, event: MotionEvent) {
        if (it.action == MotionEvent.ACTION_MOVE) {
            this.x = event.rawX - width
            this.y = event.rawY - height
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            if (dotIsSticked) {
                dotPaint.alpha = 123
                textPaint.alpha = 123
                reminderPaint.alpha = 123
            }
            drawDotCircle(canvas)
            drawDotCreatedAgo(canvas)
            drawDotReminder(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(dotSize(), dotSize())
    }

    private fun drawDotReminder(canvas: Canvas) {
        dotReminder?.takeIf { it > 0 }?.let { dotReminder ->

            val now = Calendar.getInstance().getTimeInMillis()
            val minutesRemianingSpan = (dotReminder - now)
            val minutesSpan = (dotReminder - dotCreatedDate)
            val percent = (minutesSpan - minutesRemianingSpan) * 100F / Math.max(1, minutesSpan)

            drawPart(canvas, percent)
        }
    }

    private fun drawPart(canvas: Canvas, percent: Float) {
        val x = width / 2F
        val y = height / 2F
        with(canvas) {
            save()
            rotate(-90f, x, y)

            drawArc(
                RectF(
                    REMINDER_INNER_PADDING,
                    REMINDER_INNER_PADDING,
                    (dotSize() - REMINDER_INNER_PADDING),
                    (dotSize() - REMINDER_INNER_PADDING)
                ),
                0f, percent * 3.6f, false, reminderPaint
            )

            restore()
        }
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