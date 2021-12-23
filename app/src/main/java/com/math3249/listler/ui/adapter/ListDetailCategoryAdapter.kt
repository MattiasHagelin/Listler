package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.ListDetailsCategoryBinding
import com.math3249.listler.model.ListWithItem
import com.math3249.listler.model.entity.Item

class ListDetailCategoryAdapter(private val clickListener: (Item) -> Unit,
                                private val longClickListener: (Item) -> Unit
): ListAdapter<Item, ListDetailCategoryAdapter.ListDetailCategoryHolder>(DiffCallback) {

    class ListDetailCategoryHolder(
        private var binding: ListDetailsCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Item) {
            //binding. = category
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDetailCategoryHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListDetailCategoryHolder(
            ListDetailsCategoryBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListDetailCategoryHolder, position: Int) {
        val item = getItem(position)

        toggleView(holder, item)

        holder.itemView.setOnClickListener {
            clickListener(item)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener(item)
            true
        }
        holder.bind(item)
    }

    fun toggleView(holder: ListDetailCategoryHolder, item: Item){
        /*
        if (item.isDone) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams =
                RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        }

         */
    }

}