package com.math3249.listler.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.math3249.listler.model.crossref.ListCategoryItem
//import com.math3249.listler.model.crossref.ListItem
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import kotlinx.coroutines.flow.Flow

abstract class BaseDao {
    @Query("SELECT * FROM Item")
    abstract fun getItems(): Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE name = :name")
    abstract fun getItemByName(name: String): Item?

    @Query("SELECT COUNT(*) FROM ListCategoryItem WHERE categoryName = :categoryName AND listId = :listId")
    abstract fun countItemsInCategory(listId: Long, categoryName: String): Int

    //CATEGORY
    @Query("SELECT categoryName FROM ListCategoryItem WHERE listId = :listId AND itemName = :itemName")
    abstract fun getCategoryId(listId: Long, itemName: String): String

    @Query("SELECT categoryId FROM Category WHERE name = :categoryName")
    abstract fun getCategoryId(categoryName: String): Long

    @Query("SELECT * FROM Category")
    abstract fun getCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(category: Category): Long

    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    //abstract suspend fun insert(listItem: ListItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(listCategoryItem: ListCategoryItem): Long

    suspend fun insertWithTimeStamp(listCategoryItem: ListCategoryItem): Long {
        return insert(listCategoryItem.apply {
            modifiedAt = System.nanoTime()
        })
    }

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(listCategoryItem: ListCategoryItem)

    suspend fun updateWithTimeStamp(listCategoryItem: ListCategoryItem) {
        update(listCategoryItem.apply{
            modifiedAt = System.nanoTime()
        })
    }

}