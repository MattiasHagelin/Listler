package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.ListDetailsCategoryBinding
import com.math3249.listler.model.CategoryWithItems

class ListDetailCategoryAdapter(private val clickListener: (CategoryWithItems) -> Unit,
                                private val longClickListener: (CategoryWithItems) -> Unit
): ListAdapter<CategoryWithItems, ListDetailCategoryAdapter.ListDetailCategoryHolder>(DiffCallback) {

    class ListDetailCategoryHolder(
        private var binding: ListDetailsCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryWithItems) {
            binding.category = category.category
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<CategoryWithItems>() {
        override fun areItemsTheSame(
            oldItem: CategoryWithItems,
            newItem: CategoryWithItems
        ): Boolean {
            return oldItem.category.categoryId == newItem.category.categoryId
        }

        override fun areContentsTheSame(oldCategoryWithItems: CategoryWithItems, newCategoryWithItems: CategoryWithItems): Boolean {
            return oldCategoryWithItems == newCategoryWithItems
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
        val categoryWithItems = getItem(position)

        //toggleView(holder, categoryWithItems)

        holder.itemView.setOnClickListener {
            clickListener(categoryWithItems)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener(categoryWithItems)
            true
        }
        holder.bind(categoryWithItems)
    }

    fun toggleView(holder: ListDetailCategoryHolder, CategoryWithItems: CategoryWithItems){
        /*
        if (CategoryWithItems.isDone) {
            holder.CategoryWithItemsView.visibility = View.GONE
            holder.CategoryWithItemsView.layoutParams = RecyclerView.LayoutParams(0, 0)
        } else {
            holder.CategoryWithItemsView.visibility = View.VISIBLE
            holder.CategoryWithItemsView.layoutParams =
                RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
        }

         */
    }

}