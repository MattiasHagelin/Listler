package com.math3249.listler.util.message.type

import android.os.Parcelable
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListData(
    val listName: String = "",
    val listType: String = "",
    val listItem: ListCategoryItem = ListCategoryItem(),
    val sortOrder: List<StoreCategoryCrossRef>? = null
): Parcelable