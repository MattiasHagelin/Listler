package com.math3249.listler.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.databinding.StoreCategoryBinding
import com.math3249.listler.model.StoreCategoryWithCategoryName
import com.math3249.listler.ui.adapter.StorDetailsAdapter.StorDetailHolder
import com.math3249.listler.util.utilinterface.ItemTouchHelperAdapter
import com.math3249.listler.util.utilinterface.OnStartDragListener
import java.util.*

class StorDetailsAdapter(
    private val adapterHelper: ItemTouchHelperAdapter,
    //var list: MutableList<StoreCategoryWithCategoryName>,
    var listener: OnStartDragListener
): RecyclerView.Adapter<StorDetailHolder>(), ItemTouchHelperAdapter {

    private val list: MutableList<StoreCategoryWithCategoryName> = mutableListOf()
    inner class StorDetailHolder(val binding: StoreCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(storeCategory: StoreCategoryWithCategoryName) {
            binding.storeCat = storeCategory
            binding.categoryId.text = storeCategory.storeCat.categoryId.toString()
            binding.categorySortOrder.text = storeCategory.storeCat.sortOrder.toString()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorDetailHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StorDetailHolder(
            StoreCategoryBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StorDetailHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnLongClickListener {
            listener.onStartDrag(holder)
            false
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onItemMove(from: Int, to: Int): Boolean {
        Collections.swap(list, from, to)
        notifyItemMoved(from, to)
        return true
    }

    override fun onItemDismiss(position: Int) {
        adapterHelper.onItemDismiss(position)
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearData() {
        val size = itemCount
        list.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun insertData(list: MutableList<StoreCategoryWithCategoryName>) {
        this.list.addAll(list.toTypedArray())
        notifyItemRangeInserted(0, itemCount)
    }

    fun getList(): List<StoreCategoryWithCategoryName> {
        return list
    }
}