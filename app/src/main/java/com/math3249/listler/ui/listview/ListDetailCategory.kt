package com.math3249.listler.ui.listview

import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.ui.adapter.ViewHolderFactory

class ListDetailCategory(
    override val listItem: ListCategoryItem,
): RowType {

    override fun getRowType(): Int {
        return RowTypes.CATEGORY.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val categoryViewHolder = holder as ViewHolderFactory.ListDetailCategoryHolder
        categoryViewHolder.bind(listItem.categoryName)
    }

}