package com.math3249.listler.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Item(
    @PrimaryKey(autoGenerate = true)
    val itemId: Long = 0,
    val name: String,
    //val categoryId: Long
)
