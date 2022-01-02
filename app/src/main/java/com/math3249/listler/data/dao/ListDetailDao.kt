package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.ListWithCategoriesAndItems
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
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
    fun itemExists(name: String): Boolean

    @Query("SELECT * FROM ListItemCrossRef WHERE listId = :listId AND itemId = :itemId")
    fun getListItem(listId: Long, itemId: Long): ListItemCrossRef?

    @Query("SELECT * FROM category WHERE categoryId IN (:categoryIds)")
    fun getCategories(categoryIds: List<Long>): List<Category>

    @Query("SELECT categoryId FROM listcategoryitemcrossref WHERE listId = :listId AND itemId = :itemId")
    fun getCategoryId(listId: Long, itemId: Long): Long

    @Query("SELECT COUNT(*) FROM listcategoryitemcrossref WHERE categoryId = :categoryId AND listId = :listId")
    fun countItemsInCategory(listId: Long, categoryId: Long): Int

    @Transaction
    @Query("SELECT * FROM list WHERE list.listId = :listId")
    fun getListWithCategoriesAndItemsById(listId: Long): Flow<ListWithCategoriesAndItems>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listItemCrossRef: ListItemCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listCategory: ListCategoryCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listCategoryItemCrossRef: ListCategoryItemCrossRef): Long

    @Update
    suspend fun updateItemOnList(listCategoryItemCrossRef: ListCategoryItemCrossRef)

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItemFromList(listCategoryItemCrossRef: ListCategoryItemCrossRef)

    @Delete
    suspend fun delete(listItem: ListItemCrossRef)

    @Delete
    suspend fun delete(listCategory: ListCategoryCrossRef)

    @Transaction
    suspend fun insertOrUpdate(listCategoryItemCrossRef: ListCategoryItemCrossRef) {
        if (insert(listCategoryItemCrossRef) == -1L) {
            updateItemOnList(listCategoryItemCrossRef)
        }
    }

}