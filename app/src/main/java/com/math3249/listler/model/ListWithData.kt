package com.math3249.listler.model

import androidx.room.Embedded
import androidx.room.Relation
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.entity.List

data class ListWithData(
    @Embedded val list: List,
   /* @Relation(
        parentColumn = "listId",
        entity = ListCategory::class,
        entityColumn = "listId"
    )
    val categories: kotlin.collections.List<ListCategory>,*/
    @Relation(
        parentColumn = "listId",
        entity = ListCategoryItem::class,
        entityColumn = "listId"
    )
    val listItems: kotlin.collections.List<ListCategoryItem>/*,
    @Relation(
        parentColumn = "listId",
        entity = ListItem::class,
        entityColumn = "listId"
    )
    val items: kotlin.collections.List<ListItem>
*/
)
