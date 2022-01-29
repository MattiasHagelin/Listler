/*package com.math3249.listler.model

import androidx.room.Embedded
import androidx.room.Relation
import com.math3249.listler.model.crossref.ListItem
import com.math3249.listler.model.entity.List

data class ListWithItem(
    @Embedded val list: List,
    @Relation(
        parentColumn = "listId",
        entityColumn = "listId",
        entity = ListItem::class
    )
    val items: kotlin.collections.List<ListItem>
)*/
