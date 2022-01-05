package com.math3249.listler.model

import androidx.room.Embedded
import androidx.room.Relation
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.List

data class DeleteList(
    @Embedded val list: List,
    @Relation(
        parentColumn = "listId",
        entity = ListCategoryCrossRef::class,
        entityColumn = "listId"
    )
    val categories: kotlin.collections.List<ListCategoryCrossRef>,
    @Relation(
        parentColumn = "listId",
        entity = ListCategoryItemCrossRef::class,
        entityColumn = "listId"
    )
    val listItems: kotlin.collections.List<ListCategoryItemCrossRef>,
    @Relation(
        parentColumn = "listId",
        entity = ListItemCrossRef::class,
        entityColumn = "listId"
    )
    val items: kotlin.collections.List<ListItemCrossRef>
)