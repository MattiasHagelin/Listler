package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.ListDao
import com.math3249.listler.model.entity.List
import com.math3249.listler.util.KEY_LIST_NAME
import com.math3249.listler.util.LIST_ID
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.UNCHECKED_CAST
import com.math3249.listler.util.message.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListOverviewViewModel (
    private val listDao: ListDao
        ): ViewModel() {
    val message = MutableLiveData<Message>()
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
            if (listId > 0) message.postValue(Message(
                Message.Type.LIST_INSERTED,
                true,
                mutableMapOf(LIST_ID to listId),
                mutableMapOf(KEY_LIST_NAME to name)))
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val deleteList = listDao.getDeleteList(listId)
            listDao.delete(deleteList.list,
                deleteList.categories.toTypedArray(),
                deleteList.items.toTypedArray(),
                deleteList.listItems.toTypedArray())
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


