package com.dotpad2.ui.dotdialog.colorselect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.dotpad2.R
import com.dotpad2.databinding.AdapterColorBinding
import com.dotpad2.model.ColorWrapper

class ColorSelectorAdapter(context: Context) : ArrayAdapter<ColorWrapper>(context, R.layout.adapter_color) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = when (convertView) {
            null -> DataBindingUtil.inflate(layoutInflater, R.layout.adapter_color, parent, false)
            else -> DataBindingUtil.getBinding<AdapterColorBinding>(convertView)
        } as AdapterColorBinding
        with(binding) {
            item = getItem(position)
            executePendingBindings()
            return root
        }
    }

    fun selectedItem(): ColorWrapper? {
        for (colorIndex in 0 until count) {
            val colorWrapper = getItem(colorIndex)
            if (colorWrapper != null && colorWrapper.selected) {
                return colorWrapper
            }
        }
        return null
    }

    val layoutInflater by lazy { LayoutInflater.from(context) }
}