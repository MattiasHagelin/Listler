package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.StoreBinding
import com.math3249.listler.model.entity.Store

class StoresAdapter(
    private val clickListener: (Store) -> Unit
): ListAdapter<Store, StoresAdapter.StoresHolder>(DiffCallback)  {

    class StoresHolder(
        private var binding: StoreBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(store: Store) {
            binding.store = store
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Store>() {
        override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem.storeId == newItem.storeId
        }

        override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StoresHolder(
            StoreBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StoresHolder, position: Int) {
        val store = getItem(position)

        holder.itemView.setOnClickListener {
            clickListener(store)
        }
        holder.bind(store)
    }
}