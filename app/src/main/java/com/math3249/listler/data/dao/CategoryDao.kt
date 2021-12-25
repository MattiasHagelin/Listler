package com.math3249.listler.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.math3249.listler.model.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category WHERE name = :name")
    fun getCategoryByName(name: String): Flow<Category>

    @Query ("SELECT EXISTS (SELECT 1 FROM category WHERE category.name = :name)")
    fun categoryExists(name: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category): Long
}