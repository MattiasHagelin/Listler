package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ItemDao
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import com.math3249.listler.util.*
import com.math3249.listler.util.message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val itemDao: ItemDao
): ViewModel() {
    val addItemFragmentMessage = MutableLiveData<Message>()
    val allCategories = itemDao.getCategories().asLiveData()

    fun getItem(id: Long): LiveData<Item> {
        return itemDao.getItemById(id).asLiveData()
    }

    fun addItemAndCategory(
        listId: Long,
        name: String,
        categoryName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            var itemId = itemDao.getItemIdByName(name)
            if (itemId <= 0) itemId = itemDao.insertItem(Item(name = name))

            var categoryId = itemDao.getCategoryIdByName(categoryName)
            if (categoryId <= 0L) categoryId = itemDao.insert(Category(name = categoryName))
            addItemFragmentMessage.postValue(Message(
                Message.Type.ITEM_INSERTED,
                true,
                mutableMapOf(LIST_ID to listId,
                    CATEGORY_ID to categoryId,
                    ITEM_ID to itemId)
            ))
        }
    }

    fun addItemToList(
        listId: Long,
        categoryId: Long,
        itemId: Long,
        isDone: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.insert(ListItemCrossRef(listId, itemId))
            itemDao.insert(ListCategoryCrossRef(listId, categoryId))
            itemDao.insertWithTimeStamp(ListCategoryItemCrossRef(listId, categoryId, itemId, isDone))
        }
    }

    fun updateItem(
        listId: Long,
        itemId: Long,
        categoryId: Long,
        itemName: String,
        categoryName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.update(Item(itemId, itemName))
            var catIdByName = itemDao.getCategoryIdByName(categoryName)

            //Check if Category exists
            if (catIdByName <= 0) {
                catIdByName = itemDao.insert(Category(name = categoryName))//If not insert new category
                //Add category to list
                updateCrossRef(listId, catIdByName, itemId, categoryId)
            } else
                updateCrossRef(listId, catIdByName, itemId, categoryId)
        }
    }

    private suspend fun updateCrossRef(
        listId: Long,
        catIdByName: Long,
        itemId: Long,
        categoryId: Long
    ) {
        if (!itemDao.categoryExistsInList(listId, catIdByName))
            itemDao.insert(ListCategoryCrossRef(listId, catIdByName))

        itemDao.insertOrUpdate(
            ListCategoryItemCrossRef(
                listId,
                catIdByName,
                itemId
            )
        )
        //remove from old category
        itemDao.delete(
            ListCategoryItemCrossRef(
                listId,
                categoryId,
                itemId
            )
        )
        //Remove old category from list if there's no items left in it
        if (itemDao.countItemsInCategory(listId, categoryId) <= 0)
            itemDao.delete(ListCategoryCrossRef(listId, categoryId))
    }

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

