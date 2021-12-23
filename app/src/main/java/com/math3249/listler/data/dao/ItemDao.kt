package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM Item")
    fun getItems(): Flow<List<Item>>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: Item)
    @Update
    suspend fun updateItem(item: Item)
    @Delete
    suspend fun deleteItem(item: Item)
}