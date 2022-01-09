package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ItemDao: BaseDao() {

    @Query("SELECT itemId FROM item WHERE name = :name")
    abstract fun getItemIdByName(name: String): Long

    @Query("SELECT * FROM Category")
    abstract fun getCategories(): Flow<List<Category>>

    @Query("SELECT categoryId FROM Category WHERE name = :categoryName")
    abstract fun getCategoryIdByName(categoryName: String): Long

    @Query("SELECT * FROM listcategoryitemcrossref WHERE listId = :listId AND categoryId = :categoryId AND itemId = :itemId")
    abstract fun getListCategoryItem(listId: Long, categoryId: Long, itemId: Long): ListCategoryItemCrossRef?

    @Query("SELECT categoryId FROM listcategorycrossref WHERE listId = :listId")
    abstract fun getCategoryId(listId: Long): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertItem(item: Item): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertItemToListIgnore(listItemCrossRef: ListItemCrossRef): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(category: Category): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(listCategory: ListCategoryCrossRef): Long

    @Update
    abstract suspend fun update(item: Item)

    @Update
    abstract suspend fun update(category: Category)

    @Update
    abstract suspend fun updateItemOnList(listItemCrossRef: ListItemCrossRef)

    @Delete
    abstract suspend fun deleteItem(item: Item)

    @Transaction
    open suspend fun insertOrUpdate(listCategoryItemCrossRef: ListCategoryItemCrossRef) {
        if (insert(listCategoryItemCrossRef) == -1L) {
            update(listCategoryItemCrossRef)
        }
    }
}