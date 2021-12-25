package com.math3249.listler.util

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.util.message.Type.MessageType

object Utils {
    /**
     * Get string from string resource file
     */
    fun getString(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }

    /**
     * Sets first letter to upper case
     * and the rest to lower case
     */
    fun standardizeItemName(name: String): String? {
        return if (name.isBlank()) {
            null
        } else {
            name.lowercase()
                .replaceFirstChar { it.uppercaseChar() }
        }
    }

    /**
     * creates a snackbar of short duration
     */
    private fun snackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun snackbar(type: MessageType, view: View, vararg formatArgs: Any = emptyArray()) {
        when (type) {
            MessageType.ITEM_IN_LIST -> snackbar(view, getString(R.string.i_c_item_in_list, *formatArgs))
            MessageType.ITEM_IN_DATABASE -> snackbar(view, "Item already exists")
            MessageType.ITEM_NOT_IN_DATABASE -> snackbar(view, getString(R.string.e_item_does_not_exist))
            MessageType.CATEGORY_IN_DATABASE -> snackbar(view, getString(R.string.i_category_in_db, *formatArgs))
            else -> return
        }
    }

    fun snackbar(type: MessageType, view: View) {
        when (type) {
            MessageType.ITEM_IN_LIST -> snackbar(view, getString(R.string.i_item_in_list))
            MessageType.ITEM_IN_DATABASE -> snackbar(view, "Item already exists")
            MessageType.ITEM_NOT_IN_DATABASE -> snackbar(view, getString(R.string.e_item_does_not_exist))
            MessageType.ITEM_INPUT_EMPTY -> snackbar(view, getString(R.string.i_item_input_empty))
            MessageType.CATEGORY_INPUT_EMPTY -> snackbar(view, getString(R.string.i_category_input_empty))
            else -> return
        }
    }
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