package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ItemDao
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.crossref.CategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.util.Utils
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AddItemViewModel(
    private val itemDao: ItemDao
): ViewModel() {

    val insertId = MutableLiveData<Long?>()
    val allCategories = itemDao.getCategories().asLiveData()

    fun getItem(id: Long): LiveData<Item> {
        return itemDao.getItemById(id).asLiveData()
    }

    fun addItem(
        name: String,
        categoryId: Long
    ) {
        val item = Item(
            name = name,
            categoryId = categoryId
        )
        viewModelScope.launch {
            insertId.postValue(itemDao.insertItem(item))
        }
    }

    fun addItemToList(
        itemId: Long,
        listId: Long,
        isDone: Boolean
    ) {
        val listItem = ListItemCrossRef(
            listId = listId,
            itemId = itemId,
            done = isDone
        )
        viewModelScope.launch() {
            itemDao.insertOrUpdate(listItem)
            /*
            try {
                listDetailDao.insertItemToList(itemList)
            } catch (e: SQLiteConstraintException) {
                message.postValue(Message(MessageType.ITEM_IN_LIST, false, itemId))
            }*/
        }
    }

    fun updateItem(
        id: Long,
        name: String,
        categoryId: Long
    ) {
        val item = Item(
            itemId = id,
            name = name,
            categoryId = categoryId
        )
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

    class AddItemViewModelFactory(private val itemDao: ItemDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
                @Suppress
                return AddItemViewModel(itemDao) as T
            }
            throw IllegalArgumentException(Utils.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}

