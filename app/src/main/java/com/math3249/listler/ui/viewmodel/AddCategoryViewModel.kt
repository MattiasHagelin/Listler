package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.math3249.listler.R
import com.math3249.listler.data.dao.CategoryDao
import com.math3249.listler.util.Utils
import java.lang.IllegalArgumentException

class AddCategoryViewModel(
    private val categoryDao: CategoryDao
): ViewModel() {

    //val newCategoryId: MutableLiveData<Long?>()

    fun addCategory() {
        //TODO: Implement this
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
            throw IllegalArgumentException(Utils.getString(R.string.e_unknown_viewmodel_class))
        }
    }
}