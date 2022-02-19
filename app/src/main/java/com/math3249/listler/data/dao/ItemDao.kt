package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item

@Dao
abstract class ItemDao: BaseDao() {

    @Query("SELECT itemId FROM item WHERE name = :name")
    abstract fun getItemIdByName(name: String): Long

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId AND categoryName = :categoryName AND itemName = :itemName")
    abstract fun getListCategoryItem(listId: Long, categoryName: String, itemName: String): ListCategoryItem?

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId AND itemName = :itemName")
    abstract fun getListCatItem(listId: Long, itemName: String): ListCategoryItem?
/*
    @Query("SELECT categoryId FROM ListCategory WHERE listId = :listId")
    abstract fun getCategoryId(listId: Long): Long

    @Query("SELECT categoryId FROM ListCategory WHERE name = :name")
    abstract fun getListCategoryIdByName(name: String): Long

    @Query("SELECT * FROM ListCategory WHERE categoryId = :catId")
    abstract fun getListCategory(catId: Long): ListCategory?
*/
    @Query("SELECT EXISTS (SELECT 1 FROM Item WHERE name = :itemName)")
    abstract fun itemExists(itemName: String): Boolean

    //@Query ("SELECT EXISTS (SELECT 1 FROM ListCategory WHERE categoryId = :categoryId AND listId = :listId)")
   // abstract fun categoryExistsInList(listId: Long, categoryId: Long): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM Category WHERE name = :catName)")
    abstract fun categoryExists(catName: String): Boolean
/*
    @Query("SELECT * FROM ListItem WHERE name = :name")
    abstract fun getListItemByName(name: String): ListItem
*/
    //@Query("SELECT itemId FROM ListItem WHERE name = :name")
    //abstract fun getListItemIdByName(name: String): Long

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId AND itemName = :itemName")
    abstract fun getListCategoryItem(listId: Long, itemName: String): ListCategoryItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertItem(item: Item): Long

    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    //abstract suspend fun insert(listCategory: ListCategory): Long

    @Update
    abstract suspend fun update(item: Item)

    @Update
    abstract suspend fun update(category: Category)

    //@Update(onConflict = OnConflictStrategy.IGNORE)
    //abstract suspend fun updateItemOnList(listItem: ListItem): Int

   // @Update(onConflict = OnConflictStrategy.IGNORE)
    //abstract suspend fun update(listCategory: ListCategory): Int

    @Delete
    abstract suspend fun deleteItem(item: Item)

   // @Delete
    //abstract suspend fun delete(listCategory: ListCategory)

    @Delete
    abstract suspend fun delete(listCategoryItem: ListCategoryItem)

    //@Delete
    //abstract suspend fun delete(listItem: ListItem)
/*
    @Transaction
    open suspend fun insertOrUpdate(listCategoryItem: ListCategoryItem) {
        if (insert(listCategoryItem) == -1L) {
            update(listCategoryItem)
        }
    }
    @Transaction
    open suspend fun insertOrUpdate(listItem: ListItem): Boolean {
        if (insert(listItem) == -1L) {
            if (updateItemOnList(listItem) == 0) {
                return false
            }
        }
        return true
    }
    @Transaction
    open suspend fun insertOrUpdate(listCategory: ListCategory): Boolean {
        if (insert(listCategory) == -1L) {
            if (update(listCategory) == 0) {
                return false
            }
        }
        return true
    }
 */
}