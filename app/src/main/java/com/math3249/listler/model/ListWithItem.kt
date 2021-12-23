package com.math3249.listler.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.entity.List

data class ListWithItem(
    @Embedded val list: List,
    @Relation(
        parentColumn = "listId",
        entity = ListItemCrossRef::class,
        entityColumn = "itemId",
        associateBy = (Junction(
            parentColumn = "listId",
            entityColumn = "listId",
            value = ListItemCrossRef::class
        ))
    )
    val listItems: kotlin.collections.List<ListItemCrossRef>,
    @Relation(
        parentColumn = "listId",
        entityColumn = "itemId",
        associateBy = Junction(ListItemCrossRef::class)
    )
    val items: kotlin.collections.List<Item>
)
