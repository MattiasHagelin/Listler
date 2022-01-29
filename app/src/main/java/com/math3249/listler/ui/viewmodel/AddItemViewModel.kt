package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ItemDao
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.fragment.navargs.AddItemArgs
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.UNCHECKED_CAST
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message.Type
import com.math3249.listler.util.message.type.ListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val itemDao: ItemDao
): ViewModel() {
    val message = MutableLiveData<ListMessage>()
    val allCategories = itemDao.getCategories().asLiveData()

    //fun getItem(name: String): LiveData<Item> {
      //  return itemDao.getItemByName(name).asLiveData()
    //  }

    fun addItemAndCategory(
        listId: Long,
        name: String,
        categoryName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var itemId = itemDao.getItemIdByName(name)
            if (itemId <= 0) itemId = itemDao.insertItem(Item(name = name))

            var categoryId = itemDao.getCategoryId(categoryName)
            if (categoryId <= 0) categoryId = itemDao.insert(Category(name = categoryName))

            addItemToList(listId, categoryName, name)

            message.postValue(
                ListMessage(
                Type.ITEM_INSERTED,
                true
            )
            )
        }
    }

    private fun addItemToList(
        listId: Long,
        catName: String,
        itemName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val listItem = itemDao.getListCatItem(listId, itemName)
            val catId = getCategoryId(catName)
            if (listItem != null) {
                if (listItem.categoryName != catName) {
                    itemDao.delete(listItem)
                    itemDao.insertWithTimeStamp(ListCategoryItem(listId, catId, listItem.itemId, catName, itemName))
                } else {
                    message.postValue(
                        ListMessage(
                            Type.ITEM_IN_LIST,
                            false,
                            ListData(listItem = ListCategoryItem(itemName = itemName))
                        )
                    )
                }
            } else {
                val itemId = getItemId(itemName)
                itemDao.insertWithTimeStamp(ListCategoryItem(listId, catId, itemId, catName, itemName))
            }
        }
    }

    fun updateItemOnList(
        addItemData: AddItemArgs,
        catName: String,
        itemName: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.update(
                ListCategoryItem(
                    addItemData.listId,
                    addItemData.catId,
                    addItemData.itemId,
                    catName,
                    itemName
            ))
        }
    }

    private suspend fun getItemId(itemName: String): Long {
        var itemId = itemDao.getItemIdByName(itemName)
        if (itemId <= 0) itemId = itemDao.insertItem(Item(name = itemName))
        return itemId
    }

    private suspend fun getCategoryId(catName: String): Long {
        var catId = itemDao.getCategoryIdByName(catName)
        if (catId <= 0) catId = itemDao.insert(Category(name = catName))
        return catId
    }

    private fun updateItemOnList(listItem: ListCategoryItem) {

    }
/*
    fun updateItem(
        listId: Long,
        itemId: Long,
        itemName: String,
        catName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateMessage = if (updateItem(listId, itemId, itemName)) {
                //Add category to list
                val catId = updateCategory(listId, catName)
                updateCrossRef(listId, catId, itemId)
                Message(
                    Type.ITEM_INSERTED,
                    true
                )
            } else
                Message(
                    Type.ITEM_IN_LIST,
                    false,
                    mutableMapOf(ITEM_ID to itemId),
                    mutableMapOf(ITEM_DATA to itemName)
                )
            message.postValue(updateMessage)

        }
    }

    private suspend fun updateItem(
        listId: Long,
        itemId: Long,
        itemName: String
    ): Boolean {
        if (itemDao.insertOrUpdate(ListItem(listId, itemId, itemName))) {
            //Check if Item exists or create if missing
            if (!itemDao.itemExists(itemName))
                itemDao.insertItem(Item(name = itemName))
            return true
        }
        return false
    }

    private suspend fun updateCategory(
        listId: Long,
        catName: String
    ): Long {
        var catId = itemDao.getListCategoryIdByName(catName)
        if (catId <= 0) catId = itemDao.insert(ListCategory(listId, name = catName))
        if (!itemDao.categoryExists(catName))
            itemDao.insert(Category(name = catName))
        return catId
    }

    private suspend fun updateCrossRef(
        listId: Long,
        catId: Long,
        itemId: Long
    ) {
        //Get old ListCategoryItem
        val oldListCatItem = itemDao.getListCategoryItem(listId, itemId)
        if (oldListCatItem != null) {
            //Remove old ListCategoryItem
            itemDao.delete(oldListCatItem)
            //Remove old category from list if there's no items left in it
            if (itemDao.countItemsInCategory(listId, oldListCatItem.categoryId) <= 0){
                val listCat = itemDao.getListCategory(oldListCatItem.categoryId)
                if (listCat != null)
                    itemDao.delete(listCat)
            }
        }
        //insert or update ListCategoryItem with new data
        itemDao.insertOrUpdate(
            ListCategoryItem(
                listId,
                catId,
                itemId
            )
        )
    }
*/
    class AddItemViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
                @Suppress(UNCHECKED_CAST)
                return AddItemViewModel(itemDao) as T
            }
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}

