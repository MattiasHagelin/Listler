package com.math3249.listler.model

import androidx.room.Embedded
import com.math3249.listler.model.crossref.StoreCategoryCrossRef

data class StoreCategoryWithCategoryName(
    @Embedded val storeCat: StoreCategoryCrossRef,
    val categoryName: String
)
