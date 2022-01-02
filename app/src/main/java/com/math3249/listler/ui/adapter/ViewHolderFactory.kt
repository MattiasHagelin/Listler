package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.ListDetailCategoryBinding
import com.math3249.listler.databinding.ListDetailItemBinding
import com.math3249.listler.ui.listview.RowType
import com.math3249.listler.ui.listview.RowTypes

class ViewHolderFactory {

    class ListDetailCategoryHolder(private var binding: ListDetailCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryName: String) {
            binding.categoryName.text = categoryName
            binding.executePendingBindings()
        }
    }

    class ListDetailItemHolder(private var binding: ListDetailItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: String) {
            binding.itemName.text = itemName
            binding.executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                RowTypes.CATEGORY.ordinal -> {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    ListDetailCategoryHolder(
                        ListDetailCategoryBinding.inflate(layoutInflater, parent, false)
                    )
                }
                else -> {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    ListDetailItemHolder(
                        ListDetailItemBinding.inflate(layoutInflater, parent, false)
                    )
                }
            }
        }
    }
}