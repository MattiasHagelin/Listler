package com.math3249.listler.ui.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.R
import com.math3249.listler.databinding.ListItemListBinding
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.entity.List
import com.math3249.listler.ui.listview.RowTypeKey
import com.math3249.listler.ui.listview.RowTypes
import com.math3249.listler.ui.viewmodel.ListDetailViewModel
import com.math3249.listler.ui.viewmodel.ListOverviewViewModel
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.Utils

class ListOverviewAdapter(private val clickListener: (List) -> Unit
): ListAdapter<List, ListOverviewAdapter.ListOverviewHolder>(DiffCallback) {

    class ListOverviewHolder(
        private var binding: ListItemListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(list: List) {
            binding.list = list

            //get image to ListOverview
            val imageResource = when (list.type){
                StringUtil.getString(R.string.medicine_list) -> R.drawable.ic_medication_24
                else -> R.drawable.ic_shopping_cart_24
            }
            binding.image.setImageResource(imageResource)
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<List>() {
        override fun areItemsTheSame(oldItem: List, newItem: List): Boolean {
            return oldItem.listId == newItem.listId
        }

        override fun areContentsTheSame(oldItem: List, newItem: List): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListOverviewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListOverviewHolder(
            ListItemListBinding.inflate(layoutInflater, parent, false)
        )
    }

    fun getItemsItemId(position: Int): Long {
        return getItem(position).listId
    }

    override fun onBindViewHolder(holder: ListOverviewHolder, position: Int) {
        val list = getItem(position)

        holder.itemView.setOnClickListener {
            clickListener(list)
        }
        holder.bind(list)
    }

    class Swipe(val swipeLeft: (position: Int) -> Unit): ItemTouchHelper.SimpleCallback(0,
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
                ItemTouchHelper.LEFT -> {
                    swipeLeft(viewHolder.adapterPosition)
                }
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
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            val backgroundColor = ColorDrawable(Color.RED)
            val itemView = viewHolder.itemView
/*
            backgroundColor.setBounds(0,
            itemView.top,
                (itemView.left + dX).toInt(),
            itemView.bottom)

 */
            if (dX < 0)  {
                backgroundColor.setBounds((itemView.right + dX).toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom)
/*
                icon?.setBounds(left, top, right, bottom)
                icon?.draw(canvas)
                AppCompatResources.getDrawable(R.drawable.ic_delete_24)

 */
            }



            backgroundColor.draw(c)
        }
    }
}