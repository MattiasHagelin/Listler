package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.entity.List
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ListDao: BaseDao() {
    @Query("SELECT * FROM list")
    abstract fun getAllLists(): Flow<kotlin.collections.List<List>>

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId")
    abstract suspend fun getListItems(listId: Long): kotlin.collections.List<ListCategoryItem>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(list: List): Long
    @Update
    abstract suspend fun updateList(list: List)
    @Delete
    abstract suspend fun deleteList(list: List)

    //@Delete
    //suspend fun delete(listItems: Array<ListItem>)

    //@Delete
    //suspend fun delete(listCategories: Array<ListCategory>)

    @Delete
    abstract suspend fun delete(listCategoryItem: Array<ListCategoryItem>)

    /*
    @Transaction
    suspend fun delete(list: List,
                       listCategory: Array<ListCategory>,
                       listItem: Array<ListItem>,
                       listCategoryItem: Array<ListCategoryItem>
    ) {
        delete(listCategoryItem)
        delete(listCategory)
        delete(listItem)
        deleteList(list)
    }
     */
}