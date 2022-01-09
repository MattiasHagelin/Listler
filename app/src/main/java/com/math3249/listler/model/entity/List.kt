package com.math3249.listler.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.math3249.listler.util.Type

@Entity
data class List(
    @PrimaryKey(autoGenerate = true)
    val listId: Long = 0,
    val name: String,
    val type: String = Type.SHOPPING_LIST.toString(),
    val storeId: Long
)