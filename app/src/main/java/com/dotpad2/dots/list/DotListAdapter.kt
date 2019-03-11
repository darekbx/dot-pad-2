package com.dotpad2.dots.list

import com.dotpad2.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.dotpad2.databinding.AdapterListDotBinding
import com.dotpad2.model.Dot

class DotListAdapter(context: Context) : ArrayAdapter<Dot>(context, R.layout.adapter_list_dot) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = when (convertView) {
            null -> DataBindingUtil.inflate(layoutInflater, R.layout.adapter_list_dot, parent, false)
            else -> DataBindingUtil.getBinding<AdapterListDotBinding>(convertView)
        } as AdapterListDotBinding

        with(binding) {
            dot = getItem(position)
            executePendingBindings()
            return root
        }
    }

    val layoutInflater by lazy { LayoutInflater.from(context) }
}