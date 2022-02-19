package com.math3249.listler.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ListSettings(
    @PrimaryKey
    val listId: Long,
    val completeTimeLimit: Int,
    val isHomeScreen: Boolean,
    val storeId: Long
)