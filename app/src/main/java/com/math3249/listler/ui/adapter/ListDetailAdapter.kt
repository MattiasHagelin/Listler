@file:Suppress("ReplaceCallWithBinaryOperator")

package com.math3249.listler.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.ui.listview.RowType

class ListDetailAdapter(private val clickListener: (RowType) -> Unit,
    private val longClickListener: (RowType) -> Unit = {}
): ListAdapter<RowType, RecyclerView.ViewHolder>(DiffCallback){

    companion object DiffCallback: DiffUtil.ItemCallback<RowType>(){
        override fun areItemsTheSame(oldItem: RowType, newItem: RowType): Boolean {
            return oldItem.listItem.itemId == newItem.listItem.itemId
        }

        override fun areContentsTheSame(oldItem: RowType, newItem: RowType): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(view)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener(view)
            true
        }
        view.onBindViewHolder(holder)
    }

    fun getRowType(position: Int): RowType {
        return getItem(position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].getRowType()
    }
}