package com.math3249.listler.model.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Store(
    @PrimaryKey(autoGenerate = true)
    val storeId: Long = 0,
    val name: String = ""
): Parcelable
