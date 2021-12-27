package com.math3249.listler.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.entity.List

data class ListWithCategoriesAndItems(
    @Embedded val list: List,
    @Relation(
        parentColumn = "listId",
        entityColumn = "categoryId",
        associateBy = Junction(ListCategoryCrossRef::class)
    )
    val categories: kotlin.collections.List<Category>,
    @Relation(
        parentColumn = "listId",
        entity = ListItemCrossRef::class,
        entityColumn = "itemId",
        associateBy = (Junction(
            parentColumn = "listId",
            entityColumn = "itemId",
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