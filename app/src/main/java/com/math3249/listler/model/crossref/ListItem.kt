/*package com.math3249.listler.model.crossref

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class ListItem(
    val listId: Long,
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val name: String
)
*/