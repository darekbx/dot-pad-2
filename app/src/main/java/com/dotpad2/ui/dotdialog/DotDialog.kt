package com.dotpad2.ui.dotdialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.dotpad2.R
import com.dotpad2.model.ColorWrapper
import com.dotpad2.model.SizeWrapper
import com.dotpad2.ui.dotdialog.colorselect.ColorSelectorAdapter
import com.dotpad2.ui.dotdialog.colorselect.ColorSelectorView
import com.dotpad2.ui.dotdialog.sizeselect.SizeSelectorAdapter
import com.dotpad2.ui.dotdialog.sizeselect.SizeSelectorView

class DotDialog : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_dot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareColorSelect(view)
        prepareSizeSelect(view)

        handleClickEvents()
    }

    private fun handleClickEvents() {
        view?.run {
            findViewById<View>(R.id.save_button).setOnClickListener { saveClick() }
            findViewById<View>(R.id.reset_button).setOnClickListener { resetClick() }
            findViewById<View>(R.id.reminder_button).setOnClickListener { reminderClick() }
        }
    }

    fun saveClick() {
        view?.run {
            val color = findViewById<ColorSelectorView>(R.id.color_selector).getSelectedColor()
            val size = findViewById<SizeSelectorView>(R.id.size_selector).getSelectedSize()
            Log.v("----------", "Color: $color, size: $size")
        }
    }

    fun resetClick() {

    }

    fun reminderClick() {

    }

    private fun prepareSizeSelect(view: View) {
        val sizeAdapter = SizeSelectorAdapter(view.context)
        val sizeSelector = view.findViewById<SizeSelectorView>(R.id.size_selector)
        sizeSelector.adapter = sizeAdapter
        addSizes(sizeAdapter)
    }

    private fun addSizes(sizeAdapter: SizeSelectorAdapter) {
        val sizes = provideSizes().map { SizeWrapper(it) }
        sizes.first().selected = true
        sizeAdapter.addAll(sizes)
    }

    private fun prepareColorSelect(view: View) {
        val colorAdapter = ColorSelectorAdapter(view.context)
        val colorSelector = view.findViewById<ColorSelectorView>(R.id.color_selector)
        colorSelector.adapter = colorAdapter
        addColors(view.context, colorAdapter)
    }

    private fun addColors(context: Context, colorAdapter: ColorSelectorAdapter) {
        val colors = provideColors(context).map { ColorWrapper(it) }
        colors.first().selected = true
        colorAdapter.addAll(colors)
    }

    private fun provideColors(context: Context) = with(context) {
        arrayListOf(
            getColor(R.color.dotRed),
            getColor(R.color.dotTeal),
            getColor(R.color.dotBlue),
            getColor(R.color.dotPurple),
            getColor(R.color.dotOrange),
            getColor(R.color.dotYellowrange)
        )
    }

    private fun provideSizes() = arrayOf(10, 8, 6, 5)
}