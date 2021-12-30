package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
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

    @Query("SELECT itemId FROM item WHERE name = :name")
    fun getItemIdByName(name: String): Long

    @Query("SELECT * FROM Category")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT categoryId FROM Category WHERE name = :categoryName")
    fun getCategoryIdByName(categoryName: String): Long

    @Query("SELECT * FROM listcategoryitemcrossref WHERE listId = :listId AND categoryId = :categoryId AND itemId = :itemId")
    fun getListCategoryItem(listId: Long, categoryId: Long, itemId: Long): ListCategoryItemCrossRef?

    @Query("SELECT categoryId FROM listcategoryitemcrossref WHERE listId = :listId AND itemId = :itemId")
    fun getCategoryId(listId: Long, itemId: Long): Long

    @Query("SELECT categoryId FROM listcategorycrossref WHERE listId = :listId")
    fun getCategoryId(listId: Long): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItem(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItemToListIgnore(listItemCrossRef: ListItemCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listCategory: ListCategoryCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listCategoryItemCrossRef: ListCategoryItemCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listItemCrossRef: ListItemCrossRef): Long

    @Update
    suspend fun update(item: Item)

    @Update
    suspend fun update(category: Category)

    @Update
    suspend fun update(listCategoryItemCrossRef: ListCategoryItemCrossRef)

    @Update
    suspend fun updateItemOnList(listItemCrossRef: ListItemCrossRef)

    @Delete
    suspend fun deleteItem(item: Item)

    @Transaction
    suspend fun insertOrUpdate(listCategoryItemCrossRef: ListCategoryItemCrossRef) {
        if (insert(listCategoryItemCrossRef) == -1L) {
            update(listCategoryItemCrossRef)
        }
    }
}