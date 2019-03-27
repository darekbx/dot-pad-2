package com.dotpad2.ui.dots.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

class LinearReminderView(context: Context, attributeSet: AttributeSet) : View(context,  attributeSet) {

    private val reminderPaint = Paint()

    var dotCreatedDate: Long = 0L
    var dotReminder: Long? = 0L

    init {
        reminderPaint.apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 3F
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawReminder(canvas)
    }

    private fun drawReminder(canvas: Canvas?) {
        dotReminder?.takeIf { it > 0 }?.let { dotReminder ->

            val now = Calendar.getInstance().getTimeInMillis()
            val minutesRemianingSpan = (dotReminder - now)
            val minutesSpan = (dotReminder - dotCreatedDate)
            val percent = (minutesSpan - minutesRemianingSpan) * 100F / Math.max(1, minutesSpan)
            val percentWidth = percent * (width / 100F)
            val y = 2F

            canvas?.drawLine(0F, y, percentWidth, y, reminderPaint)
        }
    }
}