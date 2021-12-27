package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.ListWithCategoriesAndItems
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDetailDao {
    @Query("SELECT * FROM Item")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE itemId = :id")
    fun getItem(id: Long): Flow<Item>

    @Query ("SELECT EXISTS (SELECT 1 FROM item WHERE item.name = :name)")
    fun itemExists(name: String): Flow<Boolean>

    @Query("SELECT * FROM category WHERE categoryId IN (:categoryIds)")
    fun getCategories(categoryIds: List<Long>): List<Category>

    @Query("SELECT categoryId FROM item WHERE itemId = :itemId")
    fun getCategoryId(itemId: Long): Long

    @Transaction
    @Query("SELECT * FROM list WHERE list.listId = :listId")
    fun getListWithCategoriesAndItemsById(listId: Long): Flow<ListWithCategoriesAndItems>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItemToList(listItemCrossRef: ListItemCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemToListIgnore(listItemCrossRef: ListItemCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listCategory: ListCategoryCrossRef): Long

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