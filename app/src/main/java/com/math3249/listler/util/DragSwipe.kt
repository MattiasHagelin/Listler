package com.math3249.listler.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.R
import com.math3249.listler.ui.adapter.ViewHolderFactory
import com.math3249.listler.util.utilinterface.Dragable
import com.math3249.listler.util.utilinterface.Swipeable

class DragSwipe<T>(
    private val swipeable: Swipeable<T>? = null,
    private val dragable: Dragable? = null
): ItemTouchHelper.SimpleCallback(
    dragable?.dragDirs ?: 0,
    swipeable?.swipeDirs ?: 0) {


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (dragable?.dragDirs == 0) return false
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        dragable?.movement(from, to)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (swipeable?.swipeDirs == 0) return
        when (direction) {
            ItemTouchHelper.LEFT -> swipeable?.swipeLeft(viewHolder.adapterPosition)
            ItemTouchHelper.RIGHT -> swipeable?.swipeRight(viewHolder.adapterPosition)
        }
        swipeable?.listAdapter?.notifyItemChanged(viewHolder.adapterPosition)
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
        val icon = getIcon(direction)
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

    private fun getIcon(direction: Int): Drawable? {
        return when (direction) {
            ItemTouchHelper.LEFT -> AppCompatResources.getDrawable(swipeable?.parentContext!!, R.drawable.ic_delete_24)
            else -> AppCompatResources.getDrawable(swipeable?.parentContext!!, R.drawable.ic_edit_24)
        }
    }
}