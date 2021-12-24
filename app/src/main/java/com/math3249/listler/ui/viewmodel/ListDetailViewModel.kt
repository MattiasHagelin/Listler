package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.CategoryWithItems
import com.math3249.listler.model.ListItem
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.Utils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ListDetailViewModel(
    private val listDetailDao: ListDetailDao
): ViewModel() {
    //Can be used to handle exceptions inside coroutine thread
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        //TODO: Log error and find possible way to inform user of error
    }

    private val categoriesWithItems = MutableLiveData<List<CategoryWithItems>>()
    val message = MutableLiveData<Message?>()
    val allItems = listDetailDao.getItems().asLiveData()

    fun itemExists(name: String): LiveData<Boolean> {
        return listDetailDao.itemExists(name).asLiveData()
    }
    fun getCategoriesWithItems(id: Long): LiveData<List<CategoryWithItems>> {
        getItems(id)
        return categoriesWithItems
    }
    private fun getItems(id: Long){
        //Do work on IO Thread
        viewModelScope.launch(Dispatchers.IO) {
            //Get selected list
            val listWithItem = listDetailDao.getSelectedList(id)

            //Convert lists in listWithItem to maps
            val itemsMappedByCategory = listWithItem.items.groupBy { it.categoryId }
            val listItemsByItemId = listWithItem.listItems.associateBy { it.itemId }

            //Get categories associated with items in listWithItem
            val categories = listDetailDao.getCategories(itemsMappedByCategory.keys.toList())

            //Use collected data to create list of Categories
            //with corresponding ListItems
            val tempCategoriesWithItems = emptyList<CategoryWithItems>().toMutableList()
            for (category in categories) {

                val listItems = mutableListOf<ListItem>()
                for (item in itemsMappedByCategory[category.categoryId]!!) {
                    listItems.add(ListItem(item.itemId, item.name, listItemsByItemId[item.itemId]!!.done))
                }
                val categoryWithItems = CategoryWithItems(category, listItems)
                tempCategoriesWithItems.add(categoryWithItems)
            }

            categoriesWithItems.postValue(tempCategoriesWithItems)
        }
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