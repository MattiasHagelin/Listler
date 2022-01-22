package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.ListWithItem
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.util.CATEGORY_ID
import com.math3249.listler.util.ITEM_ID
import com.math3249.listler.util.LIST_ID
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListDetailViewModel(
    private val listDetailDao: ListDetailDao
): ViewModel() {

    val listDetailFragmentMessage = MutableLiveData<Message?>()
    val allItems = listDetailDao.getItems().asLiveData()


    fun getListWithCategoriesAndItems(id: Long): LiveData<ListWithData> {
        return listDetailDao.getListData(id).asLiveData()
    }

    fun getListItemHistory(listId: Long): LiveData<ListWithItem> {
        return listDetailDao.getListItem(listId).asLiveData()
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
                    listDetailDao.updateWithTimeStamp(
                        ListCategoryItemCrossRef(
                            listId,
                            categoryId,
                            item.itemId,
                            false
                        )
                    )
                } else {
                    //If category is missing notify fragment
                    listDetailFragmentMessage.postValue(
                        Message(
                            Message.Type.ITEM_MISSING_CATEGORY,
                            false,
                            mutableMapOf(LIST_ID to listId,
                                CATEGORY_ID to -1,
                                ITEM_ID to item.itemId
                            ),
                            _extra = item.name
                        )
                    )
                }
            } else {
                listDetailDao.insert(ListItemCrossRef(listId, item.itemId))
                //Notify fragment item needs binding to category
                listDetailFragmentMessage.postValue(
                    Message(
                        Message.Type.ITEM_MISSING_CATEGORY,
                        false,
                        mutableMapOf(LIST_ID to listId,
                            CATEGORY_ID to -1,
                            ITEM_ID to item.itemId
                        ),
                        _extra = item.name
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
            listDetailDao.updateWithTimeStamp(
                ListCategoryItemCrossRef(
                listId,
                categoryId,
                itemId,
                isDone
            )
            )
        }
    }

    fun deleteItemFromList(listCategoryItemCrossRef: ListCategoryItemCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            val listId = listCategoryItemCrossRef.listId
            val itemId = listCategoryItemCrossRef.itemId
            val categoryId = if (listCategoryItemCrossRef.categoryId < 0)
                listDetailDao.getCategoryId(listCategoryItemCrossRef.listId, listCategoryItemCrossRef.itemId)
            else listCategoryItemCrossRef.categoryId

            //Delete item from list
            listDetailDao.delete(ListItemCrossRef(listId, itemId))

            //Check if there's more items in the same category
            if (listDetailDao.countItemsInCategory(listId, categoryId) == 1)
                //Delete category from list if there's no items in that category
                listDetailDao.delete(ListCategoryCrossRef(listId, categoryId))

            //Delete item category relation in list
            listDetailDao.deleteItemFromList(
                ListCategoryItemCrossRef(listId, categoryId, itemId)
            )
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