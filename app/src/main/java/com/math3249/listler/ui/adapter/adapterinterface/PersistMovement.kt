package com.math3249.listler.ui.adapter.adapterinterface

interface PersistMovement<T> {

    val list: MutableList<T>

    fun moveItem(from: Int, to: Int)

    fun removeItem(position: Int)
}