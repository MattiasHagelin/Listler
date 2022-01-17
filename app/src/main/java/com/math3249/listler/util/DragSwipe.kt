package com.math3249.listler.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.ui.adapter.ViewHolderFactory

class DragSwipe(
    val dragDirs: Int = 0,
    val swipeDirs: Int = 0,
    private val icon: Drawable? = null,
    val onMove: (from: Int, to: Int) -> Unit = {_,_ ->},
    val swipeLeft: (position: Int) -> Unit = {},
    val swipeRight: (position: Int) -> Unit = {}
): ItemTouchHelper.SimpleCallback(
    dragDirs,
    swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        onMove(from, to)
        if (dragDirs == 0) return false
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> swipeLeft(viewHolder.adapterPosition)
            ItemTouchHelper.RIGHT -> swipeRight(viewHolder.adapterPosition)
        }
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is ViewHolderFactory.ListDetailCategoryHolder) 0
            else super.getSwipeDirs(recyclerView, viewHolder)
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

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.scaleY = 1.3f
            viewHolder?.itemView?.alpha = 0.7f
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.scaleY = 1.0f
        viewHolder.itemView.alpha = 1.0f
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