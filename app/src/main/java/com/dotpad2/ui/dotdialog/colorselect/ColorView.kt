package com.dotpad2.ui.dotdialog.colorselect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.dotpad2.model.ColorWrapper

class ColorView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    companion object {
        val OUT_PADDING = 2F
        var CORNERS_RADIUS = 8F
        val CHECKED_BORDER_SIZE = 4F
    }

    private val paintSolid = Paint().apply { isAntiAlias = true }
    private val paintStroke = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.WHITE
        strokeWidth = CHECKED_BORDER_SIZE
    }

    var colorWrapper: ColorWrapper? = null
        set(value) {
            field = value
            field?.run {
                paintSolid.color = color
            }
        }

    fun updateChecked(isChecked: Boolean) {
        colorWrapper?.checked = isChecked
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            colorWrapper?.let {
                drawRect(this, paintSolid)
                if (it.checked) {
                    drawRect(this, paintStroke)
                }
            }
        }
    }

    private fun drawRect(canvas: Canvas, paint: Paint) {
        canvas.drawRoundRect(viewRect, CORNERS_RADIUS, CORNERS_RADIUS, paint)
    }

    private val viewRect by lazy { RectF(OUT_PADDING, OUT_PADDING, width - OUT_PADDING, height - OUT_PADDING) }
}