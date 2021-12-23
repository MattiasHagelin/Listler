package com.math3249.listler.model

data class ListCategory(
    val categoryId: Long,
    val name: String,
    val items: List<CategoryItem>
)
