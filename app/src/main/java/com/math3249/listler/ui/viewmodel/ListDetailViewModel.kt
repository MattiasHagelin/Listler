package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.ListWithItem
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.crossref.CategoryItemCrossRef
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.Utils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ListDetailViewModel(
    private val listDetailDao: ListDetailDao
): ViewModel() {
    //Can be used to handle exceptions inside coroutine thread
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        //TODO: Log error and find possible way to inform user of error
    }

    val message = MutableLiveData<Message?>()
    val allItems = listDetailDao.getItems().asLiveData()

    fun itemExists(name: String): LiveData<Boolean> {
        return listDetailDao.itemExists(name).asLiveData()
    }

    fun getItems(id: Long): LiveData<ListWithItem> {
        return listDetailDao.getSelectedList(id).asLiveData()
    }

    /**
     * Add item to list
     */
    fun addItemToList(
        listId: Long,
        itemId: Long,
        done: Boolean
    ) {
        val categoryItem = ListItemCrossRef(
            listId = listId,
            itemId = itemId,
            done = done
        )
        viewModelScope.launch() {
            listDetailDao.insertOrUpdate(categoryItem)
        }
    }

    fun updateItemOnList(
        listId: Long,
        itemId: Long,
        isDone: Boolean
    ) {
        val listItem = ListItemCrossRef(
            listId = listId,
            itemId = itemId,
            done = isDone
        )

        viewModelScope.launch {
            listDetailDao.updateItemOnList(listItem)
        }
    }

    fun deleteItemFromList(listItemCrossRef: ListItemCrossRef) {
        viewModelScope.launch {
            listDetailDao.deleteItemFromList(listItemCrossRef)
        }
    }

    class ListDetailViewModelFactory(private val listDetailDao: ListDetailDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListDetailViewModel::class.java)) {
                @Suppress
                return ListDetailViewModel(listDetailDao) as T
            }
            throw IllegalArgumentException(Utils.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}