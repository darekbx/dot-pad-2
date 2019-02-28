package com.dotpad2.ui.dotdialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.dotpad2.R
import com.dotpad2.model.ColorWrapper
import com.dotpad2.ui.dotdialog.colorselect.ColorSelectorAdapter
import com.dotpad2.ui.dotdialog.colorselect.ColorSelectorView

class DotDialog : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(context).inflate(R.layout.dialog_dot, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorAdapter = ColorSelectorAdapter(view.context)
        val colorSelector = view.findViewById<ColorSelectorView>(R.id.color_selector)
        colorSelector.adapter = colorAdapter

        val colors = provideColors(view.context).map { ColorWrapper(it, true) }
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
}