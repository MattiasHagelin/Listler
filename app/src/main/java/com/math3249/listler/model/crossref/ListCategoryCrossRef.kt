package com.math3249.listler.model.crossref

import androidx.room.Entity

@Entity(primaryKeys = ["listId", "categoryId"])
data class ListCategoryCrossRef(
    val listId: Long,
    val categoryId: Long
)
