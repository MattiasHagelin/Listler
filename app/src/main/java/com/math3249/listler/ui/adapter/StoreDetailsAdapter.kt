package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.StoreCategoryBinding
import com.math3249.listler.model.StoreCategoryWithCategoryName
import com.math3249.listler.ui.adapter.adapterinterface.PersistMovement
import java.util.*

class StoreDetailsAdapter: ListAdapter<StoreCategoryWithCategoryName, StoreDetailsAdapter.StoreDetailsHolder>(DiffCallback),
    AdapterType, PersistMovement<StoreCategoryWithCategoryName> {

    override val list = mutableListOf<StoreCategoryWithCategoryName>()

    class StoreDetailsHolder(
        private val list: MutableList<StoreCategoryWithCategoryName>,
        private val binding: StoreCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(storeCategoryWithCategoryName: StoreCategoryWithCategoryName){
            binding.storeCat = storeCategoryWithCategoryName
            binding.categoryId.text = storeCategoryWithCategoryName.storeCat.categoryId.toString()
            binding.categorySortOrder.text = storeCategoryWithCategoryName.storeCat.sortOrder.toString()
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<StoreCategoryWithCategoryName>() {
        override fun areItemsTheSame(oldItem: StoreCategoryWithCategoryName, newItem: StoreCategoryWithCategoryName): Boolean {
            return oldItem.storeCat.categoryId == newItem.storeCat.categoryId
        }

        override fun areContentsTheSame(oldItem: StoreCategoryWithCategoryName, newItem: StoreCategoryWithCategoryName): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreDetailsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StoreDetailsHolder(list,
            StoreCategoryBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StoreDetailsHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    override fun getItem(position: Int): StoreCategoryWithCategoryName {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getAdapterType(): Adapters {
        return Adapters.STORE_DETAIL_ADAPTER
    }

    override fun moveItem(from: Int, to: Int) {
        if (from < to) {
            for (i in from until to) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in from downTo to + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
    }

    override fun removeItem(position: Int) {
        list.removeAt(position)
    }

}