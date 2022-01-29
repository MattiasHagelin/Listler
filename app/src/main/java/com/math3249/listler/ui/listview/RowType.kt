package com.math3249.listler.ui.listview

import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.model.crossref.ListCategoryItem

interface RowType {

    val listItem: ListCategoryItem

    fun getRowType(): Int

    fun onBindViewHolder(holder: RecyclerView.ViewHolder)

}