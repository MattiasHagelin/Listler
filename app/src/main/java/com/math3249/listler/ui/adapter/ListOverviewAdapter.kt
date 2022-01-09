package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.R
import com.math3249.listler.databinding.ListItemListBinding
import com.math3249.listler.model.entity.List
import com.math3249.listler.util.StringUtil

class ListOverviewAdapter(private val clickListener: (List) -> Unit
): ListAdapter<List, ListOverviewAdapter.ListOverviewHolder>(DiffCallback) {

    class ListOverviewHolder(
        private var binding: ListItemListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(list: List) {
            binding.list = list

            //get image to ListOverview
            val imageResource = when (list.type){
                StringUtil.getString(R.string.medicine_list) -> R.drawable.ic_medication_24
                else -> R.drawable.ic_shopping_cart_24
            }
            binding.image.setImageResource(imageResource)
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<List>() {
        override fun areItemsTheSame(oldItem: List, newItem: List): Boolean {
            return oldItem.listId == newItem.listId
        }

        override fun areContentsTheSame(oldItem: List, newItem: List): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOverviewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListOverviewHolder(
            ListItemListBinding.inflate(layoutInflater, parent, false)
        )
    }

    fun getItemsItemId(position: Int): Long {
        return getItem(position).listId
    }

    override fun onBindViewHolder(holder: ListOverviewHolder, position: Int) {
        val list = getItem(position)

        holder.itemView.setOnClickListener {
            clickListener(list)
        }
        holder.bind(list)
    }

}