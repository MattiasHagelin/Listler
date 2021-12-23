package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDao
import com.math3249.listler.model.ListWithItem
import com.math3249.listler.model.entity.List
import com.math3249.listler.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class ListOverviewViewModel (
    private val listDao: ListDao
        ): ViewModel() {
    val allLists: LiveData<kotlin.collections.List<List>> = listDao.getAllLists().asLiveData()

    fun getList(id: Long): LiveData<ListWithItem> {
        return listDao.getListWithItemsByListId(id).asLiveData()
    }

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
            listDao.insertList(list)
        }
    }

    fun updateList(
        id: Long,
        name: String,
        type: String
    ) {
        val list = List(
            listId = id,
            name = name,
            type = type,
            storeId = 0L //TODO: Replace with StoreId from Table
        )
        viewModelScope.launch(Dispatchers.IO) {
            listDao.updateList(list)
        }
    }

    fun deleteList(list: List) {
        viewModelScope.launch(Dispatchers.IO) {
            listDao.deleteList(list)
        }
    }

    //TODO: Validate input
    class ListOverviewViewModelFactory(private val listDao: ListDao): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListOverviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ListOverviewViewModel(listDao) as T
            }
            throw IllegalArgumentException(Utils.getString(R.string.e_unknown_viewmodel_class))
        }

    }
}


