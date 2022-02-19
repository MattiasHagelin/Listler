package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDao
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.entity.ListSettings
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.type.ListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(
    private val listDao: ListDao
): ViewModel() {

    val message = MutableLiveData<ListMessage>()
    val allItems = listDao.getItems().asLiveData()
    val allLists: LiveData<List<com.math3249.listler.model.entity.List>> = listDao.getAllLists().asLiveData()
    val allCategories = listDao.getCategories().asLiveData().value
    val stores = listDao.stores().asLiveData()

    fun getListWithCategoriesAndItems(id: Long): LiveData<ListWithData> {
        return listDao.getListData(id).asLiveData()
    }

    fun getSortOrder(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val settings = listDao.getListSetting(listId)
            val store = settings?.let { listDao.getStore(it.storeId) }
            message.postValue(
                ListMessage(
                    Message.Type.LIST_SORT_ORDER,
                    true,
                    ListData(
                        sortOrder = store
                    )
                )
            )
        }
    }

    /*fun getHomeScreen(){
        viewModelScope.launch(Dispatchers.IO) {
            val homeList = listDao.getHomeScreen()
            if (homeList.isNullOrEmpty()){
                val homeList = listDao.getHomeScreen()
                if (homeList != null && homeList.isNotEmpty()) {
                    val homeListId = homeList.first().listId
                    val list = listDao.getList(homeListId)
                    message.postValue(
                        ListMessage(
                            Message.Type.LIST_HOME_SCREEN,
                            true,
                            ListData(
                                list.name,
                                list.type,
                                ListCategoryItem(list.listId)
                            )
                        )
                    )
                }
            }
        }
    }*/

    fun getListItemHistory(listId: Long): LiveData<List<ListCategoryItem>> {
        return listDao.getListItems(listId).asLiveData()
    }

    fun getListSettings(listId: Long): LiveData<ListSettings>{
        return listDao.getListSettings(listId).asLiveData()
    }

    fun insertOrUpdate(listSettings: ListSettings) {
        viewModelScope.launch(Dispatchers.IO) {
            if (listSettings.isHomeScreen){
               /* listDao.getHomeScreen()?.forEach { listSettings ->
                    listDao.update(ListSettings(
                        listSettings.listId,
                        listSettings.completeTimeLimit,
                        false,
                        listSettings.storeId
                    ))
                }*/
            }
            listDao.insertOrUpdate(listSettings)
        }
    }

    fun addItemToList(listItem: ListCategoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!listDao.itemExists(listItem.itemName))
                listDao.insert(Item(name = listItem.itemName))
            if (listDao.isItemInList(listItem.listId, listItem.itemName)) {
                listDao.updateWithTimeStamp(listItem)
            }
            else
                listDao.insertWithTimeStamp(listItem)
        }
    }

    /**
     * Add item to list
     */
    fun addItemToList(
        listId: Long,
        itemName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (listDao.isItemInList(listId, itemName)){
                val listItem = listDao.getListItem(listId, itemName)!!
                if (listItem.categoryName.isNotBlank()) {
                    //If item have a Category do an update
                    listDao.updateWithTimeStamp(
                        ListCategoryItem(
                            listItem.listId,
                            listItem.categoryName,
                            listItem.itemName
                        ))
                } else {
                    //If category is missing notify fragment
                    message.postValue(
                        ListMessage(
                            Message.Type.ITEM_NEW,
                            false,
                            ListData(listItem = ListCategoryItem(listId, itemName = itemName))
                        )
                    )
                }
            } else {
                message.postValue(
                    ListMessage(
                        Message.Type.ITEM_NEW,
                        false,
                        ListData(listItem = ListCategoryItem(listId, itemName = itemName))
                    )
                )
            }
        }
    }

    fun insert(listItem: ListCategoryItem, isNew: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNew)
                listDao.insertOrUpdate(
                    Item(name = listItem.itemName))

            listDao.insertWithTimeStamp(listItem)
        }
    }

    fun updateItemOnList(
        listItem: ListCategoryItem,
        isDone: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            listDao.updateWithTimeStamp(
                ListCategoryItem(
                    listItem.listId,
                    listItem.categoryName,
                    listItem.itemName,
                    isDone
                ))
        }
    }

    fun deleteListItems(listItems: List<ListCategoryItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            listDao.delete(listItems)
        }
    }

    fun deleteItemFromList(listCategoryItem: ListCategoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            listDao.deleteItemFromList(listCategoryItem)
        }
    }

    fun addList(
        name: String,
        type: String
    ){
        val list = com.math3249.listler.model.entity.List(
            name = name,
            type = type
        )
        viewModelScope.launch {
            val listId = listDao.insert(list)
            if (listId > 0) {
                listDao.insert(ListSettings(
                    listId,
                    1,
                    false,
                    1
                ))
                message.postValue(ListMessage(
                    Message.Type.LIST_INSERTED,
                    true,
                    ListData(name, type, ListCategoryItem(listId = listId))
                ))
            }
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val listItems = listDao.getItemsOnList(listId)
            if (listItems != null)
                listDao.delete(listItems)
            listDao.deleteList(com.math3249.listler.model.entity.List(listId, "", ""))
        }
    }

    class ListViewModelFactory(private val listDao: ListDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
                @Suppress
                return ListViewModel(listDao) as T
            }
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}