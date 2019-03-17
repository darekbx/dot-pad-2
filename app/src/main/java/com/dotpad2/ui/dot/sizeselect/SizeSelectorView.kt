package com.dotpad2.ui.dot.sizeselect

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import com.dotpad2.extensions.childLoop

class SizeSelectorView(context: Context, attrs: AttributeSet?)
    : AdapterView<SizeSelectorAdapter>(context, attrs) {

    companion object {
        val DESIRED_ITEMS_COUNT = 4
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
            val childSize = calculateChildSize().toInt()
            var xPosition = calculateStartOffset(childSize)

            for (sizeIndex in 0 until adapter.count) {
                val view = adapter.getView(sizeIndex, null, this)
                with(view) {
                    layout(xPosition, 0, xPosition + childSize, childSize)
                    setOnClickListener { onColorTap(view) }
                }
                addViewInLayout(view, sizeIndex, null, true)

                xPosition += childSize + ITEM_PADDING
            }
        }
    }

    private fun calculateStartOffset(childSize: Int) =
        (childSize * 2) + (ITEM_PADDING * 2)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val childSize = calculateLayoutWidth(widthSize).toInt()
        setMeasuredDimension(
            (childSize + ITEM_PADDING) * adapter.count,
            childSize
        )
    }

    var selectedSize: Int?
        get() = adapter.selectedItem()?.size ?: null
        set(value) {
            value?.let { sizeToSet ->
                selectBySize { it == sizeToSet }
            }
        }

    private fun onColorTap(view: View) {
        val position = indexOfChild(view)
        selectByIndex { it == position }
    }

    private fun selectByIndex(predictor: ((index: Int) -> Boolean)) {
        childLoop<SizeView> { index, sizeView ->
            sizeView.updateSelected(predictor(index))
        }
    }

    private fun selectBySize(predictor: ((size: Int) -> Boolean)) {
        childLoop<SizeView> { index, sizeView ->
            val size = sizeView.sizeWrapper?.size ?: 0
            sizeView.updateSelected(predictor(size))
        }
    }

    private fun calculateLayoutWidth(parentWidth: Int = width) =
        parentWidth / (DESIRED_ITEMS_COUNT * 1.5) - ITEM_PADDING

    private fun calculateChildSize(parentWidth: Int = width) =
        parentWidth / (adapter.count * 1.5)- ITEM_PADDING
}