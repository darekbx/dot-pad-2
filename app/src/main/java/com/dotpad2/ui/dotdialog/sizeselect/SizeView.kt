package com.dotpad2.ui.dotdialog.sizeselect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.dotpad2.R
import com.dotpad2.model.SizeWrapper
import com.dotpad2.ui.dotdialog.colorselect.ColorView

class SizeView(context: Context, attributeSet: AttributeSet) : ColorView(context, attributeSet) {

    protected val textPaint =
        Paint().apply {
            isAntiAlias = true
            color = Color.LTGRAY
            textAlign = Paint.Align.CENTER
            textSize = 32F
            typeface = Typeface.MONOSPACE
        }

    var sizeWrapper: SizeWrapper? = null
        set(value) {
            field = value
            field?.run {
                solidPaint.color = context.getColor(R.color.grey)
            }
        }

    override fun updateSelected(isSelected: Boolean) {
        sizeWrapper?.selected = isSelected
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            sizeWrapper?.let {
                drawRect(this)
                drawSize(this, it.size)
                if (it.selected) {
                    drawTick(this)
                }
            }
        }
    }

    private fun drawSize(canvas: Canvas, size: Int) {
        val text = "${size}"
        val yOffset = computeTextYOffset(text)
        val centerX = width / 2F
        val centerY = height / 2F + yOffset
        canvas.drawText(text, centerX, centerY, textPaint)
    }

    private fun computeTextYOffset(text: String): Float {
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val yOffset = textBounds.height() / 2F
        return yOffset
    }
}