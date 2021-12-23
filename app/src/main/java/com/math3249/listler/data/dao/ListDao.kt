package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.ListWithItem
import com.math3249.listler.model.entity.List
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {
    @Query("SELECT * FROM list")
    fun getAllLists(): Flow<kotlin.collections.List<List>>
    @Transaction
    @Query("SELECT * FROM list WHERE listId = :id")
    fun getListWithItemsByListId(id: Long): Flow<ListWithItem>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertList(list: List)
    @Update
    suspend fun updateList(list: List)
    @Delete
    suspend fun deleteList(list: List)
}