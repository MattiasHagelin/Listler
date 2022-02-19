package com.math3249.listler.model

import androidx.room.Embedded
import androidx.room.Relation
import com.math3249.listler.model.entity.ListSettings
import com.math3249.listler.model.entity.Store

data class ListSettingsWithSortOrder(
    @Embedded val listSettings: ListSettings,
    @Relation(
        parentColumn = "storeId",
        entityColumn = "storeId",
        entity = Store::class

    )
    val store: StoreWithCategories?
)
