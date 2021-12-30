package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.CategoryWithItems
import com.math3249.listler.model.ListItem
import com.math3249.listler.model.ListWithCategoriesAndItems
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.fragment.ListDetailsFragmentDirections
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.Utils
import com.math3249.listler.util.message.Type.MessageType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
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


    fun getListWithCategoriesAndItems(id: Long): LiveData<ListWithCategoriesAndItems> {
        return listDetailDao.getListWithCategoriesAndItemsById(id).asLiveData()
    }

    /**
     * Add item to list
     */
    fun addItemToList(
        listId: Long,
        item: Item
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            //Check is in list
            val listItem = listDetailDao.getListItem(listId, item.itemId)
            if (listItem != null) {
                //Check if item have a category
                val categoryId = listDetailDao.getCategoryId(listId, item.itemId)
                if (categoryId > 0) {
                    //If item have a Category do an update
                    listDetailDao.updateItemOnList(
                        ListCategoryItemCrossRef(
                            listId,
                            categoryId,
                            item.itemId,
                            false
                        )
                    )
                } else {
                    //If category is missing notify fragment
                    message.postValue(
                        Message(
                            MessageType.ITEM_MISSING_CATEGORY,
                            false,
                            listId,
                            -1,
                            item.itemId,
                            item.name
                        )
                    )
                }
            } else {
                listDetailDao.insert(ListItemCrossRef(listId, item.itemId))
                //Notify fragment item needs binding to category
                message.postValue(
                    Message(
                        MessageType.ITEM_MISSING_CATEGORY,
                        false,
                        listId,
                        -1,
                        item.itemId,
                        item.name
                    )
                )
            }
        }
    }

    fun updateItemOnList(
        listId: Long,
        itemId: Long,
        isDone: Boolean
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val categoryId = listDetailDao.getCategoryId(listId, itemId)
            listDetailDao.updateItemOnList(ListCategoryItemCrossRef(
                listId,
                categoryId,
                itemId,
                isDone
            ))
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
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}