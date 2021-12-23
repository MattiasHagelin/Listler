package com.math3249.listler.model.crossref

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["categoryId", "itemId"], unique = true)])
data class CategoryItemCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val categoryId: Long,
    val itemId: Long
)
