package com.math3249.listler.ui.adapter

import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.ui.listview.RowType
import com.math3249.listler.ui.listview.RowTypeKey
import com.math3249.listler.ui.listview.RowTypes
import com.math3249.listler.ui.viewmodel.ListDetailViewModel

class ListDetailAdapter(private val clickListener: (RowType) -> Unit,
    private val longClickListener: (RowType) -> Unit
): ListAdapter<RowType, RecyclerView.ViewHolder>(DiffCallback){

    companion object DiffCallback: DiffUtil.ItemCallback<RowType>(){
        override fun areItemsTheSame(oldItem: RowType, newItem: RowType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RowType, newItem: RowType): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(view)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener(view)
            true
        }
        view.onBindViewHolder(holder)
    }

    fun getRowType(position: Int): RowType {
        return getItem(position)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].getRowType()
    }
/*
    class Swipe(val listId: Long,
                val viewModel: ListDetailViewModel,
                private val adapter: ListDetailAdapter): ItemTouchHelper.SimpleCallback(0,
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
            val position = viewHolder.adapterPosition;
            val item = adapter.getItem(position)
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    if (item.getRowType() == RowTypes.ITEM.ordinal) {
                        viewModel.deleteItemFromList(ListCategoryItemCrossRef(
                            listId = listId,
                            itemId = item.getData()[RowTypeKey.ITEM_ID]?.toLongOrNull() ?: -1L,
                            categoryId = item.getData()[RowTypeKey.CATEGORY_ID]?.toLongOrNull() ?: -1L
                        ))
                    }

                }
            }
        }

    }
 */
}