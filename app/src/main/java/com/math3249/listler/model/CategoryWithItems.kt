package com.math3249.listler.model

import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item

data class CategoryWithItems(
    val category: Category,
    val items: List<Item>
)
