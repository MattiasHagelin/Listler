package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.ListHistoryItemBinding
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.util.UNCHECKED_CAST

//import com.math3249.listler.util.UNCHECKED_CAST

class ListHistoryAdapter(): ListAdapter<ListCategoryItem, ListHistoryAdapter.ListHistoryHolder>(DiffCallback), Filterable {

    private var  filterList: MutableList<ListCategoryItem>? = mutableListOf()
    private var removeList: MutableList<ListCategoryItem> = mutableListOf()

    companion object DiffCallback: DiffUtil.ItemCallback<ListCategoryItem>() {
        override fun areItemsTheSame(oldItem: ListCategoryItem, newItem: ListCategoryItem): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: ListCategoryItem, newItem: ListCategoryItem): Boolean {
            return oldItem == newItem
        }
    }

    class ListHistoryHolder(
        private val binding: ListHistoryItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListCategoryItem) {
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
        if (removeList.contains(item)) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            holder.itemView.isEnabled = true
        }
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return filterList?.size!!
    }

    override fun getItem(position: Int): ListCategoryItem {
        return filterList?.get(position)!!
    }

    fun getSelectedItem(position: Int): ListCategoryItem {
        return getItem(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                @Suppress(UNCHECKED_CAST)
                filterList = results.values as MutableList<ListCategoryItem>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                //var filteredResults: List<ListItem>? = null
                val filteredResults = if (constraint.isEmpty()) {
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

    fun remove(position: Int) {
        removeList.add(filterList?.get(position)!!)
       // currentList.toMutableList().removeAt(position)
       // filterList?.removeAt(position)
        //notifyItemRemoved(position)
        //notifyItemRangeChanged(position, itemCount)
    }

    fun delete(): List<ListCategoryItem> {
        return removeList.toList()
    }

    override fun submitList(list: MutableList<ListCategoryItem>?) {
        super.submitList(list)
        filterList = list
    }

    private fun getFilteredResults(constraint: String?): List<ListCategoryItem> {
        val results: MutableList<ListCategoryItem> = ArrayList()
        for (item in filterList!!) {
            if (item.itemName.lowercase().contains(constraint!!))
                results.add(item)
        }
        return results
    }
}