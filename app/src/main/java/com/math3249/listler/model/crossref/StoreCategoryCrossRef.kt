package com.math3249.listler.model.crossref

import androidx.room.Entity

@Entity(primaryKeys = ["storeId", "categoryId"])
data class StoreCategoryCrossRef(
    val storeId: Long = 0,
    val categoryId: Long = 0,
    val sortOrder: Int
)
