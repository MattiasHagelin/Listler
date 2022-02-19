package com.math3249.listler.model.crossref

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(primaryKeys = ["storeId", "categoryId"])
data class StoreCategoryCrossRef(
    val storeId: Long = 0,
    val categoryId: Long = 0,
    val sortOrder: Long = -1
): Parcelable
