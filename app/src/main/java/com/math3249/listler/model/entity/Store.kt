package com.math3249.listler.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Store(
    @PrimaryKey(autoGenerate = true)
    val storeId: Long = 0,
    val name: String
)
