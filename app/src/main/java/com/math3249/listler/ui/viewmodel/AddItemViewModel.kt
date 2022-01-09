package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ItemDao
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.Type.MessageType
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
                MessageType.ITEM_INSERTED,
                true,
                listId,
                categoryId,
                itemId
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
        itemName: String,
        categoryId: Long,
        categoryName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.update(Item(itemId, itemName))
            var categoryId = categoryId

            //Check if Category exists
            if (categoryId <= 0) {
                categoryId = itemDao.getCategoryIdByName(categoryName)
                if (categoryId <= 0) categoryId = itemDao.insert(Category(name = categoryName))//If not insert new category


                //Add category to list
                itemDao.insert(ListCategoryCrossRef(listId, categoryId))
            } else {
                //Update Category
                itemDao.update(Category(categoryId, categoryName))
            }

            //Update or insert ListCategoryItem
            itemDao.insertOrUpdate(
                ListCategoryItemCrossRef(
                listId,
                categoryId,
                itemId,
                false
            )
            )
        }
    }

    class AddItemViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
                @Suppress
                return AddItemViewModel(itemDao) as T
            }
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}

