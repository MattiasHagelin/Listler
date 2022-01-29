/*package com.math3249.listler.model.crossref

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class ListCategory(
    val listId: Long,
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0,
    val name: String
)
*/