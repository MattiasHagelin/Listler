package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.ListWithCategoriesAndItems
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ListDetailDao: BaseDao() {

    @Query ("SELECT EXISTS (SELECT 1 FROM item WHERE item.name = :name)")
    abstract fun itemExists(name: String): Boolean

    @Query("SELECT * FROM ListItemCrossRef WHERE listId = :listId AND itemId = :itemId")
    abstract fun getListItem(listId: Long, itemId: Long): ListItemCrossRef?

    @Query("SELECT COUNT(*) FROM listcategoryitemcrossref WHERE categoryId = :categoryId AND listId = :listId")
    abstract fun countItemsInCategory(listId: Long, categoryId: Long): Int

    @Transaction
    @Query("SELECT * FROM list WHERE list.listId = :listId")
    abstract fun getListWithCategoriesAndItemsById(listId: Long): Flow<ListWithCategoriesAndItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(listCategory: ListCategoryCrossRef): Long

    @Delete
    abstract suspend fun deleteItemFromList(listCategoryItemCrossRef: ListCategoryItemCrossRef)

    @Delete
    abstract suspend fun delete(listItem: ListItemCrossRef)

    @Delete
    abstract suspend fun delete(listCategory: ListCategoryCrossRef)

}