package com.dotpad2.ui.dots

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.dotpad2.R
import com.dotpad2.databinding.AdapterDotBinding
import com.dotpad2.model.Dot

class DotAdapter(context: Context) : ArrayAdapter<Dot>(context, R.layout.adapter_dot) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = when (convertView) {
            null -> DataBindingUtil.inflate(layoutInflater, R.layout.adapter_dot, parent, false)
            else -> DataBindingUtil.getBinding<AdapterDotBinding>(convertView)
        } as AdapterDotBinding
        with(binding) {
            dot = getItem(position)
            executePendingBindings()
            return root
        }
    }

    val layoutInflater by lazy { LayoutInflater.from(context) }
}