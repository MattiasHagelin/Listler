package com.math3249.listler.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.ui.RowType

class ListDetailAdapter():
    ListAdapter<RowType, RecyclerView.ViewHolder>(DiffCallback){

    var listData: List<RowType> = emptyList()

    companion object DiffCallback: DiffUtil.ItemCallback<RowType>(){
        override fun areItemsTheSame(oldItem: RowType, newItem: RowType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RowType, newItem: RowType): Boolean {
            return oldItem.equals(newItem)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        listData[position].onBindViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return listData[position].getRowType()
    }

}