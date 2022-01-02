package com.math3249.listler.ui.listview

import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

interface RowType {

    val id: Long

    fun getRowType(): Int

    fun getData(): Map<RowTypeKey, String>

    fun onBindViewHolder(holder: RecyclerView.ViewHolder)

}