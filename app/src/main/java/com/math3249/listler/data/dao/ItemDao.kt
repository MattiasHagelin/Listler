package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM Item")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE itemId = :itemId")
    fun getItemById(itemId: Long): Flow<Item>

    @Query("SELECT * FROM Category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT categoryId FROM category WHERE name = :name")
    fun getCategoryIdByName(name: String): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemToListIgnore(listItemCrossRef: ListItemCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category): Long

    @Update
    suspend fun updateItem(item: Item)

    @Update
    suspend fun updateItemOnList(listItemCrossRef: ListItemCrossRef)

    @Delete
    suspend fun deleteItem(item: Item)

    @Transaction
    suspend fun insertOrUpdate(listItemCrossRef: ListItemCrossRef) {
        if (insertItemToListIgnore(listItemCrossRef) == -1L) {
            updateItemOnList(listItemCrossRef)
        }
    }
}