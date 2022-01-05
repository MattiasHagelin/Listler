package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.DeleteList
import com.math3249.listler.model.ListWithItem
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.entity.List
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {
    @Query("SELECT * FROM list")
    fun getAllLists(): Flow<kotlin.collections.List<List>>
    @Transaction
    @Query("SELECT * FROM list WHERE listId = :id")
    fun getListWithItemsByListId(id: Long): Flow<ListWithItem>

    @Transaction
    @Query("SELECT * FROM list WHERE listId = :listId")
    fun getDeleteList(listId: Long): DeleteList

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List): Long
    @Update
    suspend fun updateList(list: List)
    @Delete
    suspend fun deleteList(list: List)

    @Delete
    suspend fun delete(listItems: Array<ListItemCrossRef>)

    @Delete
    suspend fun delete(listCategories: Array<ListCategoryCrossRef>)

    @Delete
    suspend fun delete(listCategoryItemCrossRef: Array<ListCategoryItemCrossRef>)

    @Transaction
    suspend fun delete(list: List,
        listCategoryCrossRef: Array<ListCategoryCrossRef>,
        listItemCrossRef: Array<ListItemCrossRef>,
        listCategoryItemCrossRef: Array<ListCategoryItemCrossRef>
    ) {
        delete(listCategoryItemCrossRef)
        delete(listCategoryCrossRef)
        delete(listItemCrossRef)
        deleteList(list)
    }
}