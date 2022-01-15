package com.math3249.listler.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import kotlinx.coroutines.flow.Flow

abstract class BaseDao {
    @Query("SELECT * FROM Item")
    abstract fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE itemId = :itemId")
    abstract fun getItemById(itemId: Long): Flow<Item>


    //CATEGORY
    @Query("SELECT categoryId FROM listcategoryitemcrossref WHERE listId = :listId AND itemId = :itemId")
    abstract fun getCategoryId(listId: Long, itemId: Long): Long

    @Query("SELECT categoryId FROM Category WHERE name = :categoryName")
    abstract fun getCategoryIdByName(categoryName: String): Long

    @Query("SELECT * FROM Category")
    abstract fun getCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(listItemCrossRef: ListItemCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(listCategoryItemCrossRef: ListCategoryItemCrossRef): Long

    suspend fun insertWithTimeStamp(listCategoryItemCrossRef: ListCategoryItemCrossRef): Long {
        return insert(listCategoryItemCrossRef.apply {
            modifiedAt = System.nanoTime()
        })
    }

    @Update
    abstract suspend fun update(listCategoryItemCrossRef: ListCategoryItemCrossRef)

    suspend fun updateWithTimeStamp(listCategoryItemCrossRef: ListCategoryItemCrossRef) {
        update(listCategoryItemCrossRef.apply{
            modifiedAt = System.nanoTime()
        })
    }
}