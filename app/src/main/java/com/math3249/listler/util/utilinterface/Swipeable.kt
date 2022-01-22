package com.math3249.listler.util.utilinterface

import android.content.Context
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


interface Swipeable<T> {
    val swipeDirs: Int
    val parentContext: Context
    val listAdapter: ListAdapter<T, RecyclerView.ViewHolder>
    fun swipeLeft(position: Int)
    fun swipeRight(position: Int)
}