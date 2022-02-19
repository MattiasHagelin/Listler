package com.math3249.listler.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.R
import com.math3249.listler.util.utilinterface.ItemTouchHelperAdapter
import com.math3249.listler.util.utilinterface.ItemTouchHelperViewHolder

class ListItemTouchHelperCallback(
    private val adapter: ItemTouchHelperAdapter,
    private val dragable: Boolean = false,
    private val swipable: Boolean = false
): ItemTouchHelper.Callback() {
    companion object {
        const val ALPHA_FULL = 1.0f
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = if (dragable && recyclerView.layoutManager is GridLayoutManager)
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
        else if (dragable)
            ItemTouchHelper.UP or ItemTouchHelper.DOWN
        else 0

        val swipeFlags = if (swipable) ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        else 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.itemViewType != target.itemViewType)
            return false
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
            val itemView = viewHolder.itemView
            if (dX < 0) {
                setBackground(itemView, dX, c, "#BA1B1B", ItemTouchHelper.LEFT)
                setIcon(itemView, c, ItemTouchHelper.LEFT)
            }
            if (dX > 0) {
                setBackground(itemView, dX, c, "#1FA4D1", ItemTouchHelper.RIGHT)
                setIcon(itemView, c, ItemTouchHelper.RIGHT)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        //viewHolder.itemView.scaleY = ALPHA_FULL
        viewHolder.itemView.alpha = ALPHA_FULL
        if (viewHolder is ItemTouchHelperViewHolder) {
            (viewHolder as ItemTouchHelperViewHolder).onItemClear()
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //viewHolder?.itemView?.scaleY = 1.3f
            viewHolder?.itemView?.alpha = 0.7f
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    private fun setBackground(view: View, dX: Float, c: Canvas, colorHex: String, direction: Int) {
        val color = Color.parseColor(colorHex)
        val background: ColorDrawable
        when (direction) {
            ItemTouchHelper.LEFT -> {
                background = ColorDrawable(color)
                background.setBounds(
                    (view.right + dX).toInt(),
                    view.top,
                    view.right,
                    view.bottom
                )
            }
            else -> {
                background = ColorDrawable(color)
                background .setBounds(
                    (view.left + dX).toInt(),
                    view.top,
                    view.left,
                    view.bottom
                )
            }
        }
        background.draw(c)
    }

    private fun setIcon(view: View, c: Canvas, direction: Int) {
        val icon = getIcon(view.context, direction)
        val intrinsicHeight = (icon?.intrinsicWidth ?: 0)
        val xMarkTop = view.top + ((view.bottom - view.top) - intrinsicHeight) / 2
        val xMarkBottom = xMarkTop + intrinsicHeight

        when (direction) {
            ItemTouchHelper.LEFT -> {
                icon?.setBounds(
                    view.right - 2 * icon.intrinsicWidth,
                    xMarkTop,
                    view.right - icon.intrinsicWidth,
                    xMarkBottom
                )
            }
            else -> {
                icon?.setBounds(
                    view.left + icon.intrinsicWidth,
                    xMarkTop,
                    view.left + 2 * icon.intrinsicWidth,
                    xMarkBottom
                )
            }
        }
        icon?.draw(c)
    }

    private fun getIcon(context: Context,direction: Int): Drawable? {
        return when (direction) {
            ItemTouchHelper.LEFT -> AppCompatResources.getDrawable(context, R.drawable.ic_delete_24)
            else -> AppCompatResources.getDrawable(context, R.drawable.ic_edit_24)
        }
    }
}