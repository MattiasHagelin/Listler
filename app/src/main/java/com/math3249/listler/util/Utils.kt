package com.math3249.listler.util

import android.view.View
import android.widget.AutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import com.math3249.listler.R
import com.math3249.listler.util.message.Message.Type

object Utils {
    /**
     * creates a snackbar of short duration
     */
    private fun snackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun snackbar(type: Type, view: View, vararg formatArgs: Any = emptyArray()) {
        when (type) {
            Type.ITEM_IN_LIST -> snackbar(view, StringUtil.getString(R.string.i_c_item_in_list, *formatArgs))
            Type.ITEM_IN_DATABASE -> snackbar(view, "Item already exists")
            Type.ITEM_NOT_IN_DATABASE -> snackbar(view, StringUtil.getString(R.string.e_item_does_not_exist))
            Type.CATEGORY_IN_DATABASE -> snackbar(view, StringUtil.getString(R.string.i_category_in_db, *formatArgs))
            Type.INVALID_INPUT -> snackbar(view, StringUtil.getString(R.string.i_empty_input, *formatArgs))
            else -> return
        }
    }

    fun snackbar(type: Type, view: View) {
        when (type) {
            Type.ITEM_IN_LIST -> snackbar(view, StringUtil.getString(R.string.i_item_in_list))
            Type.ITEM_IN_DATABASE -> snackbar(view, "Item already exists")
            Type.ITEM_NOT_IN_DATABASE -> snackbar(view, StringUtil.getString(R.string.e_item_does_not_exist))
            Type.ITEM_INPUT_EMPTY -> snackbar(view, StringUtil.getString(R.string.i_item_input_empty))
            Type.CATEGORY_INPUT_EMPTY -> snackbar(view, StringUtil.getString(R.string.i_category_input_empty))
            Type.INVALID_INPUT -> snackbar(view, StringUtil.getString(R.string.i_list_name_empty))//TODO: make more flexible to handle all types of empty name errors
            else -> return
        }
    }

    fun clearDropdown(dropDown: AutoCompleteTextView) {
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