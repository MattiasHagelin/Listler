package com.math3249.listler.model.crossref

import androidx.room.Entity

@Entity(primaryKeys = ["listId", "itemId"])
data class ListItemCrossRef(
    val listId: Long,
    val itemId: Long
)
