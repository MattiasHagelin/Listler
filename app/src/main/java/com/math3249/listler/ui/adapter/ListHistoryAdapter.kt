package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.ListHistoryItemBinding
import com.math3249.listler.model.entity.Item
import com.math3249.listler.util.UNCHECKED_CAST

class ListHistoryAdapter: ListAdapter<Item, ListHistoryAdapter.ListHistoryHolder>(DiffCallback), Filterable {

    private var filterList: List<Item> = currentList

    companion object DiffCallback: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    class ListHistoryHolder(
        private val binding: ListHistoryItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHistoryHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListHistoryHolder(
            ListHistoryItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListHistoryHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun getItem(position: Int): Item {
        return filterList[position]
    }

    fun getSelectedItem(position: Int): Item {
        return getItem(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                @Suppress(UNCHECKED_CAST)
                filterList = results.values as MutableList<Item>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                var filteredResults: List<Item>? = null
                filteredResults = if (constraint.isEmpty()) {
                    currentList
                } else {
                    getFilteredResults(constraint.toString().lowercase())
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }
        }
    }

    fun updateList(items: List<Item>){
        submitList(items)
        filterList = items
    }

    protected fun getFilteredResults(constraint: String?): List<Item> {
        val results: MutableList<Item> = ArrayList()
        for (item in currentList) {
            if (item.name.lowercase().contains(constraint!!))
                results.add(item)
        }
        return results
    }
}