package com.math3249.listler.model.crossref

import androidx.room.Entity

@Entity(primaryKeys = ["listId", "categoryId", "itemId"])
data class ListCategoryItemCrossRef(
    val listId: Long,
    val categoryId: Long,
    val itemId: Long,
    val done: Boolean = false,
    var modifiedAt: Long = 0,
    var sortOrder: Long = -1
)
