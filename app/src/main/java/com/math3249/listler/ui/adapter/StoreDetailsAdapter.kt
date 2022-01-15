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

class StoreDetailsAdapter(override var _list: MutableList<StoreCategoryWithCategoryName>) : ListAdapter<StoreCategoryWithCategoryName, StoreDetailsAdapter.StoreDetailsHolder>(DiffCallback),
    AdapterType, PersistMovement<StoreCategoryWithCategoryName> {

    class StoreDetailsHolder(
        private val list: MutableList<StoreCategoryWithCategoryName>,
        private val binding: StoreCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(storeCategoryWithCategoryName: StoreCategoryWithCategoryName){
            list.add(storeCategoryWithCategoryName)
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

    override fun getAdapterType(): Adapters {
        return Adapters.STORE_DETAIL_ADAPTER
    }

    override fun moveItem(from: Int, to: Int) {
        Collections.swap(list, from, to)
    }

    override fun removeItem(position: Int) {
        list.removeAt(position)
    }

    override val list get() = _list

}