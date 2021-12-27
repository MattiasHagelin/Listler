package com.math3249.listler.ui

import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.ui.adapter.ViewHolderFactory

class ListDetailCategory(private val categoryName: String,
                         override val id: Long): RowType {

    override fun getRowType(): Int {
        return RowType.RowTypes.CATEGORY.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val categoryViewHolder = holder as ViewHolderFactory.ListDetailCategoryHolder
        categoryViewHolder.bind(categoryName)
    }

}