package com.math3249.listler.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.math3249.listler.R
import com.math3249.listler.data.dao.StoreDao
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.UNCHECKED_CAST

class StoreDetailsViewModel(
    private val storeDao: StoreDao
): ViewModel() {

    class StoreDetailsViewModelFactory(private val storeDao: StoreDao): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StoreDetailsViewModel::class.java)) {
                @Suppress(UNCHECKED_CAST)
                return StoreDetailsViewModel(storeDao) as T
            }
            throw IllegalArgumentException(StringUtil.getString(R.string.e_unknown_viewmodel_class))
        }

    }
}