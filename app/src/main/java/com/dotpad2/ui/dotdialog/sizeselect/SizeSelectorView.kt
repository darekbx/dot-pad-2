package com.dotpad2.ui.dotdialog.sizeselect

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView

class SizeSelectorView(context: Context, attrs: AttributeSet?)
    : AdapterView<SizeSelectorAdapter>(context, attrs) {

    companion object {
        val DESIRED_ITEMS_COUNT = 6
        val ITEM_PADDING = 5
    }

    private lateinit var sizeSelectorAdapter: SizeSelectorAdapter

    override fun getAdapter(): SizeSelectorAdapter = sizeSelectorAdapter

    override fun setAdapter(adapter: SizeSelectorAdapter?) {
        adapter?.let {
            this.sizeSelectorAdapter = it
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
                    layout(xPosition, 0, xPosition + childSize, childSize)
                    setOnClickListener { onColorTap(view) }
                }
                addViewInLayout(view, colorIndex, null, true)

                xPosition += childSize + ITEM_PADDING
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val childSize = calculateLayoutWidth(widthSize)
        setMeasuredDimension(
            (childSize + ITEM_PADDING) * adapter.count,
            childSize)
    }

    fun getSelectedSize() = adapter.selectedItem()?.size ?: null

    private fun onColorTap(view: View) {
        val position = indexOfChild(view)
        for (sizeIndex in 0 until adapter.count) {
            val sizeView = getChildAt(sizeIndex) as SizeView
            sizeView.updateSelected(position == sizeIndex)
        }
    }

    private fun calculateLayoutWidth(parentWidth: Int = width) =
        parentWidth / DESIRED_ITEMS_COUNT - ITEM_PADDING

    private fun calculateChildSize(parentWidth: Int = width) =
        parentWidth / adapter.count - ITEM_PADDING
}