package com.math3249.listler.util

import android.view.View
import android.widget.AutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import com.math3249.listler.R
import com.math3249.listler.util.message.type.MessageType

object Utils {
    /**
     * creates a snackbar of short duration
     */
    private fun snackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun snackbar(type: MessageType, view: View, vararg formatArgs: Any = emptyArray()) {
        when (type) {
                MessageType.ITEM_IN_LIST -> snackbar(view, StringUtil.getString(R.string.i_c_item_in_list, *formatArgs))
            MessageType.ITEM_IN_DATABASE -> snackbar(view, "Item already exists")
            MessageType.ITEM_NOT_IN_DATABASE -> snackbar(view, StringUtil.getString(R.string.e_item_does_not_exist))
            MessageType.CATEGORY_IN_DATABASE -> snackbar(view, StringUtil.getString(R.string.i_category_in_db, *formatArgs))
            else -> return
        }
    }

    fun snackbar(type: MessageType, view: View) {
        when (type) {
            MessageType.ITEM_IN_LIST -> snackbar(view, StringUtil.getString(R.string.i_item_in_list))
            MessageType.ITEM_IN_DATABASE -> snackbar(view, "Item already exists")
            MessageType.ITEM_NOT_IN_DATABASE -> snackbar(view, StringUtil.getString(R.string.e_item_does_not_exist))
            MessageType.ITEM_INPUT_EMPTY -> snackbar(view, StringUtil.getString(R.string.i_item_input_empty))
            MessageType.CATEGORY_INPUT_EMPTY -> snackbar(view, StringUtil.getString(R.string.i_category_input_empty))
            else -> return
        }
    }

    fun clearItemDropdown(dropDown: AutoCompleteTextView) {
        dropDown.setText("", false)
    }
    /*
    fun messageHandler(message: Message,
                       navController: NavController,
                       action: NavDirections
    ) {
        when (message.type) {
            MessageType.LIST_INSERTED -> navController.navigate(action)
            MessageType.ITEM_INSERTED ->
        }
    }
     */
/*
    fun getListCategories(listWithCategoryAndItems: ListWithCategoryAndItems): List<ListCategory?> {
        val categoryByCategoryId: Map<Long, List<Category>> = listWithCategoryAndItems.categories.groupBy { it.categoryId }
        val itemsByItemId: Map<Long, List<Item>> = listWithCategoryAndItems.items.groupBy { it.itemId }
        val itemListByIdCrossRef: Map<Long, ListItemCrossRef> = listWithCategoryAndItems.listItems.associateBy { it.categoryId }
        val categoryItems = itemsByItemId.flatMap { (itemId, items) ->
            items.mapNotNull { item ->
                itemListByIdCrossRef[itemId]?.let { itemList ->
                    CategoryItem(itemId, item.name, itemList.done, itemList.categoryId)
                }
            }
        }

        return result
    }
 */
}