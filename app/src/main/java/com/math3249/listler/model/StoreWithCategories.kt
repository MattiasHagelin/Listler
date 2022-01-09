package com.math3249.listler.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Store

data class StoreWithCategories(
    @Embedded val store: Store,
    @Relation(
        parentColumn = "storeId",
        entityColumn = "categoryId",
        associateBy = Junction(StoreCategoryCrossRef::class)
    )
    val categories: List<Category>,
    @Relation(
        parentColumn = "listId",
        entityColumn = "listId",
        entity = StoreCategoryCrossRef::class
    )
    val storeCategories: List<StoreCategoryCrossRef>
)
