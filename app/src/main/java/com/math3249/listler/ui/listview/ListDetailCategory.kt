package com.math3249.listler.ui.listview

import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.ui.adapter.ViewHolderFactory

class ListDetailCategory(//private val clickListener: (Long) -> Unit, // Long
                         //private val longClickListener: (Long) -> Unit,
    private val categoryName: String,
    override val id: Long
): RowType {

    override fun getRowType(): Int {
        return RowTypes.CATEGORY.ordinal
    }

    override fun getData(): Map<RowTypeKey, String> {
        return mapOf(
            RowTypeKey.CATEGORY_ID to id.toString(),
            RowTypeKey.CATEGORY to categoryName
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder) {
        val categoryViewHolder = holder as ViewHolderFactory.ListDetailCategoryHolder
        categoryViewHolder.bind(categoryName)
        /*
        Implementation for click listeners on Category headers on list
        holder.itemView.setOnClickListener{
            clickListener(categoryViewHolder.categoryId)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener(categoryViewHolder.categoryId)
            true
        }
         */
    }

}