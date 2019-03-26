package com.dotpad2.ui.archive

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.dotpad2.databinding.AdapterArchiveBinding
import com.dotpad2.model.Dot

class ArchiveAdapter(val onLongPress: (dot: Dot?) -> Unit)
    : RecyclerView.Adapter<ArchiveAdapter.ArchiveViewHolder>() {

    private val sortedList = SortedList<Dot>(Dot::class.java, object : SortedList.Callback<Dot>() {

        override fun areItemsTheSame(item1: Dot?, item2: Dot?) = item1?.equals(item2) ?: false

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun compare(o1: Dot?, o2: Dot?) =
            if (o1 != null && o2 != null) {
                o2.createdDate.compareTo(o1.createdDate)
            } else {
                0
            }

        override fun areContentsTheSame(oldItem: Dot?, newItem: Dot?) =
            if (oldItem != null && newItem != null) {
                oldItem.id == newItem.id
            } else {
                false
            }

    })

    fun addAll(dots: List<Dot>) {
        sortedList.addAll(dots)
    }

    fun replaceAll(dots: List<Dot>) {
        sortedList.beginBatchedUpdates()

        for (i in sortedList.size() - 1 downTo 0) {
            val dot = sortedList[i]
            if (!dots.contains(dot)) {
                sortedList.remove(dot)
            }
        }

        sortedList.addAll(dots)
        sortedList.endBatchedUpdates()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterArchiveBinding.inflate(inflater, parent, false)
        return ArchiveViewHolder(binding, onLongPress)
    }

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {
        holder.bind(sortedList[position])
    }

    override fun getItemCount() = sortedList.size()

    class ArchiveViewHolder(val binding: AdapterArchiveBinding, val onLongPress: (dot: Dot?) -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { onLongPress(binding.dot) }
        }

        fun bind(dot: Dot) {
            binding.dot = dot
            binding.executePendingBindings()
        }
    }
}