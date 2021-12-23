package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.crossref.CategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.util.Utils
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AddItemViewModel(
    private val listDetailDao: ListDetailDao
): ViewModel() {

    val insertId = MutableLiveData<Long?>()

    fun getItem(id: Long): LiveData<Item> {
        return listDetailDao.getItem(id).asLiveData()
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
            insertId.postValue(listDetailDao.insertItem(item))
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
            listDetailDao.insertOrUpdate(listItem)
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
            listDetailDao.updateItem(item)
        }
    }

    class AddItemViewModelFactory(private val listDetailDao: ListDetailDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddItemViewModel::class.java)) {
                @Suppress
                return AddItemViewModel(listDetailDao) as T
            }
            throw IllegalArgumentException(Utils.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}

