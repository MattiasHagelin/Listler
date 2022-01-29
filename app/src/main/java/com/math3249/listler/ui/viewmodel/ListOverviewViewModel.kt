package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDao
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.entity.List
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.UNCHECKED_CAST
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.type.ListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListOverviewViewModel (
    private val listDao: ListDao
        ): ViewModel() {
    val message = MutableLiveData<ListMessage>()
    val allLists: LiveData<kotlin.collections.List<List>> = listDao.getAllLists().asLiveData()

    fun addList(
        name: String,
        type: String
    ){
        val list = List(
            name = name,
            type = type,
            storeId = 0L //TODO: Replace with StoreId from Table
        )
        viewModelScope.launch {
            val listId = listDao.insert(list)
            if (listId > 0) message.postValue(ListMessage(
                Message.Type.LIST_INSERTED,
                true,
                ListData(name, type, ListCategoryItem(listId = listId))
            ))
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val listItems = listDao.getListItems(listId)
            if (listItems != null)
                listDao.delete(listItems.toTypedArray())
            listDao.deleteList(List(listId, "", "", 0))
        }
    }

    //TODO: Validate input
    class ListOverviewViewModelFactory(private val listDao: ListDao): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListOverviewViewModel::class.java)) {
                @Suppress(UNCHECKED_CAST)
                return ListOverviewViewModel(listDao) as T
            }
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }

    }
}


