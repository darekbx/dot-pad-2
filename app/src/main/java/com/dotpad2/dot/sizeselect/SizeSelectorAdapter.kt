package com.dotpad2.dot.sizeselect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.dotpad2.R
import com.dotpad2.databinding.AdapterSizeBinding
import com.dotpad2.model.SizeWrapper

class SizeSelectorAdapter(context: Context) : ArrayAdapter<SizeWrapper>(context, R.layout.adapter_size) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = when (convertView) {
            null -> DataBindingUtil.inflate(layoutInflater, R.layout.adapter_size, parent, false)
            else -> DataBindingUtil.getBinding<AdapterSizeBinding>(convertView)
        } as AdapterSizeBinding
        with(binding) {
            item = getItem(position)
            executePendingBindings()
            return root
        }
    }

    fun selectedItem(): SizeWrapper? {
        for (colorIndex in 0 until count) {
            val sizeWrapper = getItem(colorIndex)
            if (sizeWrapper != null && sizeWrapper.selected) {
                return sizeWrapper
            }
        }
        return null
    }

    val layoutInflater by lazy { LayoutInflater.from(context) }
}