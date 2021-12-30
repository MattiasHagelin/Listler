package com.math3249.listler.ui.listview

import androidx.recyclerview.widget.RecyclerView

interface RowType {


    val id: Long

    fun getRowType(): Int

    fun onBindViewHolder(holder: RecyclerView.ViewHolder)

}