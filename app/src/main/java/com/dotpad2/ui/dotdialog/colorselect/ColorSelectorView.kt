package com.dotpad2.ui.dotdialog.colorselect

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView

class ColorSelectorView(context: Context, attrs: AttributeSet?)
    : AdapterView<ColorSelectorAdapter>(context, attrs) {

    companion object {
        val COLOR_PADDING = 5
    }

    private lateinit var colorSelectorAdapter: ColorSelectorAdapter

    override fun getAdapter(): ColorSelectorAdapter = colorSelectorAdapter

    override fun setAdapter(adapter: ColorSelectorAdapter?) {
        adapter?.let {
            this.colorSelectorAdapter = it
        }
    }

    override fun getSelectedView() = null

    override fun setSelection(position: Int) {}

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            val childSize = calculateChildSize()
            var xPosition = 0

            for (colorIndex in 0 until adapter.count) {
                val view = adapter.getView(colorIndex, null, this)
                with(view) {
                    measure( MeasureSpec.EXACTLY, MeasureSpec.EXACTLY)
                    layout(xPosition, 0, xPosition + childSize, childSize)
                    setOnClickListener { onColorTap(view) }
                }
                addViewInLayout(view, colorIndex, null, true)

                xPosition += childSize + COLOR_PADDING
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val childSize = calculateChildSize(widthSize)
        setMeasuredDimension(width, childSize)
    }

    private fun onColorTap(view: View) {
        val position = indexOfChild(view)
        for (colorIndex in 0 until adapter.count) {
            val colorView = getChildAt(colorIndex) as ColorView
            colorView.updateChecked(position == colorIndex)
        }
    }

    private fun calculateChildSize(parentWidth: Int = width) =
        parentWidth / adapter.count - COLOR_PADDING
}