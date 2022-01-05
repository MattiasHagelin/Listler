package com.math3249.listler.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class Swipe(
    private val icon: Drawable? = null,
    val swipeLeft: (position: Int) -> Unit
): ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT
            or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> swipeLeft(viewHolder.adapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        if (dX < 0) {
            setBackground(itemView, dX, c, Color.RED)
            setIcon(itemView, c, icon)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun setBackground(view: View, dX: Float, c: Canvas, @ColorInt color: Int) {
        val background = ColorDrawable(color)
        background.setBounds(
            (view.right + dX).toInt(),
            view.top,
            view.right,
            view.bottom
        )
        background.draw(c)
    }

    private fun setIcon(view: View, c: Canvas, icon: Drawable?) {
        val intrinsicHeight = (icon?.intrinsicWidth ?: 0)
        val xMarkTop = view.top + ((view.bottom - view.top) - intrinsicHeight) / 2
        val xMarkBottom = xMarkTop + intrinsicHeight
        icon?.setBounds(
            view.right - 2 * icon.intrinsicWidth,
            xMarkTop,
            view.right - icon.intrinsicWidth,
            xMarkBottom
        )
        icon?.draw(c)
    }
}