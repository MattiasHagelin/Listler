package com.math3249.listler.util.utilinterface

interface ItemTouchHelperAdapter {
    fun onItemMove(from: Int, to: Int): Boolean
    fun onItemDismiss(position: Int)
}