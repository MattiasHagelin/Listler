package com.math3249.listler.ui

import androidx.recyclerview.widget.RecyclerView

interface RowType {
    enum class RowTypes {
        CATEGORY,
        ITEM
    }

    val id: Long

    fun getRowType(): Int

    fun onBindViewHolder(holder: RecyclerView.ViewHolder)

}