package com.math3249.listler.model.crossref

import androidx.room.Entity

@Entity(primaryKeys = ["listId", "itemId"]
/*foreignKeys = [
    ForeignKey(entity = List::class,
        parentColumns = ["listId"],
        childColumns = ["listId"],
        onDelete = CASCADE),
    ForeignKey(entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = CASCADE),
    ForeignKey(entity = Item::class,
        parentColumns = ["itemId"],
        childColumns = ["itemId"],
        onDelete = CASCADE)
]*/)
data class ListItemCrossRef(
    val listId: Long,
    val itemId: Long,
    val done: Boolean = false
)
