package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.*
import com.math3249.listler.R
import com.math3249.listler.data.dao.CategoryDao
import com.math3249.listler.model.entity.Category
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AddCategoryViewModel(
    private val categoryDao: CategoryDao
): ViewModel() {

    val insertId = MutableLiveData<Long?>()

    fun exist(name: String): LiveData<Boolean> {
        return categoryDao.categoryExists(name).asLiveData()
    }

    fun addCategory(name: String) {
        val category = Category(
            name = name
        )
        viewModelScope.launch(Dispatchers.IO) {
            insertId.postValue(categoryDao.insert(category))
        }
    }

    fun update() {
        //TODO: Implement this
    }

    fun delete() {
        //TODO: Implement this
    }
    class AddCategoryViewModelFactory(
        private val categoryDao: CategoryDao
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddCategoryViewModel::class.java)) {
                @Suppress
                return AddCategoryViewModel(categoryDao) as T
            }
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}