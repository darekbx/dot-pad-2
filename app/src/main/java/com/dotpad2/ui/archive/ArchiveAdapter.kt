package com.dotpad2.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotpad2.databinding.AdapterArchiveBinding
import com.dotpad2.model.Dot

class ArchiveAdapter(val dots: List<Dot>) : RecyclerView.Adapter<ArchiveAdapter.ArchiveViewHolder>() {

    class ArchiveViewHolder(val binding: AdapterArchiveBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dot: Dot) {
            binding.dot = dot
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterArchiveBinding.inflate(inflater, parent, false)
        return ArchiveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {
        holder.bind(dots[position])
    }

    override fun getItemCount() = dots.size
}