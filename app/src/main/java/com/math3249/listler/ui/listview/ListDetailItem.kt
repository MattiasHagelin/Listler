package com.math3249.listler.ui.listview

import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.ui.adapter.ViewHolderFactory

class ListDetailItem(
    override val listItem: ListCategoryItem,
    ): RowType {

    override fun getRowType(): Int {
        return RowTypes.ITEM.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val itemViewHolder = holder as ViewHolderFactory.ListDetailItemHolder
        itemViewHolder.bind(listItem.itemName)
    }
}