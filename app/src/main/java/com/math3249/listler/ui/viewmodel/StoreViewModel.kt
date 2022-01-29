package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.StoreDao
import com.math3249.listler.model.StoreCategoryWithCategoryName
import com.math3249.listler.model.StoreWithCategories
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Store
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.UNCHECKED_CAST
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.StoreMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StoreViewModel(
    private val storeDao: StoreDao
): ViewModel() {

    val message = MutableLiveData<StoreMessage>()

    val stores: LiveData<List<Store>>
        get() = storeDao.getStores().asLiveData()

    val categories: LiveData<List<Category>>
        get() = storeDao.getCategories().asLiveData()

    fun getStoreWithCategories(storeId: Long): LiveData<StoreWithCategories> {
        return storeDao.getStoreWithCategories(storeId).asLiveData()
    }

    fun getStoreCategories(storeId: Long): LiveData<List<StoreCategoryWithCategoryName>> {
        return storeDao.getStoreCategories(storeId).asLiveData()
    }

    fun addStore(store: Store) {
        viewModelScope.launch {
            val storeId = storeDao.insert(store)
            if (storeId > 0)
                message.postValue(StoreMessage(
                    Message.Type.STORE_INSERTED,
                    true,
                    Store(storeId, store.name)
                ))
        }
    }

    fun delete(store: Store) {
        viewModelScope.launch {
            storeDao.delete(store)
        }
    }

    fun addCategoryToStore(storeId: Long, name: String, sortOrder: Long){
        viewModelScope.launch(Dispatchers.IO) {
            var categoryId = storeDao.getCategoryId(name)
            if (categoryId <= 0) categoryId = storeDao.insert(Category(name = name))//If not insert new category

            storeDao.insert(
                StoreCategoryCrossRef(
                    storeId,
                    categoryId,
                    sortOrder
            ))
        }
    }

    fun updateSortOrder(storeId: Long, storeCategoryWithCatNames: List<StoreCategoryWithCategoryName>) {
        viewModelScope.launch(Dispatchers.IO) {
            val categorySortOrder = storeCategoryWithCatNames.mapIndexed { index, storeCat ->
                StoreCategoryCrossRef(storeCat.storeCat.storeId, storeCat.storeCat.categoryId, index.toLong())
            }
            storeDao.update(categorySortOrder)
        }
    }

    fun deleteCategory(storeCategoryCrossRef: StoreCategoryCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            storeDao.delete(storeCategoryCrossRef)
        }
    }

    class StoreViewModelFactory(private val storeDao: StoreDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
                @Suppress(UNCHECKED_CAST)
                return StoreViewModel(storeDao) as T
            }
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }

    }
}