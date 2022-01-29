package com.math3249.listler.model.crossref

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["listId", "categoryId", "itemId"])
data class ListCategoryItem(
    val listId: Long = 0,
    val categoryId: Long = 0,
    val itemId: Long = 0,
    val categoryName: String = "",
    val itemName: String = "",
    val done: Boolean = false,
    var modifiedAt: Long = 0,
    var sortOrder: Long = -1
): Parcelable
