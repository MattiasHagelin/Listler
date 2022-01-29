package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.type.ListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListDetailViewModel(
    private val listDetailDao: ListDetailDao
): ViewModel() {

    val listDetailFragmentMessage = MutableLiveData<Message>()
    val allItems = listDetailDao.getItems().asLiveData()


    fun getListWithCategoriesAndItems(id: Long): LiveData<ListWithData> {
        return listDetailDao.getListData(id).asLiveData()
    }

    fun getListItemHistory(listId: Long): LiveData<List<ListCategoryItem>> {
        return listDetailDao.getListItems(listId).asLiveData()
    }

    /**
     * Add item to list
     */
    fun addItemToList(
        listId: Long,
        itemName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val  listItem = listDetailDao.getListItem(listId, itemName)
            //Check is in list
            if (listItem != null) {
                //Check if item have a category
                if (listItem.categoryName != "") {
                    //If item have a Category do an update
                    listDetailDao.updateWithTimeStamp(
                        ListCategoryItem(
                            listItem.listId,
                            listItem.categoryId,
                            listItem.itemId
                    ))
                } else {
                    //If category is missing notify fragment
                    listDetailFragmentMessage.postValue(
                        ListMessage(
                            Message.Type.ITEM_MISSING_CATEGORY,
                            false,
                            ListData(listItem = ListCategoryItem(itemName = listItem.itemName))
                        ))
                }
            } else {
                val item = listDetailDao.getItemByName(itemName)
                if (item != null) {
                    listDetailFragmentMessage.postValue(
                        ListMessage(
                            Message.Type.ITEM_MISSING_CATEGORY,
                            false,
                            ListData(listItem = ListCategoryItem(itemName = item.name))
                        )
                    )
                } else {
                    //Notify fragment item needs binding to category
                    listDetailFragmentMessage.postValue(
                        ListMessage(
                            Message.Type.ITEM_MISSING_CATEGORY,
                            false,
                            ListData(listItem =  ListCategoryItem(itemName = itemName))
                        ))
                }

            }
        }
    }

    fun updateItemOnList(
        listItem: ListCategoryItem,
        isDone: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            listDetailDao.updateWithTimeStamp(
                ListCategoryItem(
                listItem.listId,
                listItem.categoryId,
                listItem.itemId,
                listItem.categoryName,
                listItem.itemName,
                isDone
            ))
        }
    }

    fun deleteListItems(listItems: List<ListCategoryItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            listDetailDao.delete(listItems)
        }
    }

    fun deleteItemFromList(listCategoryItem: ListCategoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val listId = listCategoryItem.listId
            val itemId = listCategoryItem.itemId
            val categoryId = if (listCategoryItem.categoryId < 0)
                listDetailDao.getCategoryId(listCategoryItem.listId, listCategoryItem.itemId)
            else listCategoryItem.categoryId

            //Delete item from list
            //listDetailDao.delete(ListItem(listId, itemId, ""))

            //Check if there's more items in the same category
            //if (listDetailDao.countItemsInCategory(listId, categoryId) == 1)
                //Delete category from list if there's no items in that category
               // listDetailDao.delete(ListCategory(listId, categoryId, ""))

            //Delete item category relation in list
            listDetailDao.deleteItemFromList(listCategoryItem)
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