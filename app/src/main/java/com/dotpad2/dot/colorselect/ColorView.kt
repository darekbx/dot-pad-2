package com.dotpad2.dot.colorselect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.dotpad2.model.ColorWrapper

open class ColorView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        val OUT_PADDING = 2F
        var CORNERS_RADIUS = 8F
        val CHECK_STROKE_WIDTH = 4F
    }

    protected val solidPaint = Paint().apply { isAntiAlias = true }
    protected val strokePaint =
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.WHITE
            textSize = 14F
            strokeWidth = CHECK_STROKE_WIDTH
        }

    private val checkPath =
        Path().apply {
            moveTo(0F, 8F)
            lineTo(6F, 14F)
            lineTo(22F, 0F)
        }

    var colorWrapper: ColorWrapper? = null
        get
        set(value) {
            field = value
            field?.run {
                solidPaint.color = color
            }
        }

    open fun updateSelected(isSelected: Boolean) {
        colorWrapper?.selected = isSelected
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            colorWrapper?.let {
                drawRect(this)
                if (it.selected) {
                    drawTick(this)
                }
            }
        }
    }

    protected fun drawRect(canvas: Canvas) {
        canvas.drawRoundRect(viewRect,
            CORNERS_RADIUS,
            CORNERS_RADIUS, solidPaint)
    }

    protected fun drawTick(canvas: Canvas) {
        val (xOffset, yOffset) = computeTickOffset()
        with(canvas) {
            save()
            translate(xOffset, yOffset)
            drawPath(checkPath, strokePaint)
            restore()
        }
    }

    private fun computeTickOffset(): Pair<Float, Float> {
        val xOffset = (width - tickBounds.width()) - CORNERS_RADIUS * 2
        return Pair(xOffset, CORNERS_RADIUS * 2)
    }

    private val tickBounds by lazy {
        var tickBounds = RectF()
        checkPath.computeBounds(tickBounds, false)
        return@lazy tickBounds
    }

    private val viewRect by lazy {
        RectF(
            OUT_PADDING,
            OUT_PADDING, width - OUT_PADDING, height - OUT_PADDING
        )
    }
}