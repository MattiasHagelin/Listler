package com.math3249.listler.ui

import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.ui.adapter.ViewHolderFactory

class ListDetailItem(private val itemName: String, override val id: Long): RowType {

    override fun getRowType(): Int {
        return RowType.RowTypes.ITEM.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val itemViewHolder = holder as ViewHolderFactory.ListDetailItemHolder
        itemViewHolder.bind(itemName)
    }

}