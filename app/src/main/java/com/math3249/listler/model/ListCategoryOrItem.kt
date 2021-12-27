package com.math3249.listler.model

data class ListCategoryOrItem(
    val categoryId: Long,
    val categoryName: String,
    val itemId: Long,
    val itemName: String,
    val done: Boolean
)