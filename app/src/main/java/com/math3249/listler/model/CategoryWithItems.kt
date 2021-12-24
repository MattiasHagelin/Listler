package com.math3249.listler.model

import com.math3249.listler.model.entity.Category
import kotlin.collections.List

data class CategoryWithItems(
    val category: Category,
    val items: List<ListItem>
)
