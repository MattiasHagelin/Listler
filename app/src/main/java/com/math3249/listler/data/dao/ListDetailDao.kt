package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.ListWithItem
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.crossref.CategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDetailDao {
    @Query("SELECT * FROM Item")
    fun getItems(): Flow<List<Item>>
    @Query("SELECT * FROM Item WHERE itemId = :id")
    fun getItem(id: Long): Flow<Item>
    @Query ("SELECT EXISTS (SELECT 1 FROM item WHERE item.name = :name)")
    fun itemExists(name: String): Flow<Boolean>
    @Transaction
    @Query("SELECT * FROM list WHERE list.listId = :listId")
    fun getSelectedList(listId: Long): Flow<ListWithItem>
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItemToList(listItemCrossRef: ListItemCrossRef)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemToListIgnore(listItemCrossRef: ListItemCrossRef): Long
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: Item): Long
    @Update
    suspend fun updateItemOnList(listItemCrossRef: ListItemCrossRef)
    @Update
    suspend fun updateItem(item: Item)
    @Delete
    suspend fun deleteItemFromList(listItemCrossRef: ListItemCrossRef)
    @Transaction
    suspend fun insertOrUpdate(listItemCrossRef: ListItemCrossRef) {
        if (insertItemToListIgnore(listItemCrossRef) == -1L) {
            updateItemOnList(listItemCrossRef)
        }
    }

}