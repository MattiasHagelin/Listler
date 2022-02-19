package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.entity.List
import com.math3249.listler.model.entity.ListSettings
import com.math3249.listler.model.entity.Store
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ListDao: BaseDao() {
    @Query("SELECT * FROM list")
    abstract fun getAllLists(): Flow<kotlin.collections.List<List>>

    @Query ("SELECT EXISTS (SELECT 1 FROM item WHERE item.name = :name)")
    abstract fun itemExists(name: String): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM ListCategoryItem WHERE listId = :listId AND itemName = :itemName)")
    abstract fun isItemInList(listId: Long, itemName: String): Boolean

    @Query("SELECT * FROM Store")
    abstract fun stores(): Flow<kotlin.collections.List<Store>>

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId AND itemName = :itemName")
    abstract fun getListItem(listId: Long, itemName: String): ListCategoryItem?

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId")
    abstract fun getListItems(listId: Long): Flow<kotlin.collections.List<ListCategoryItem>>

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId")
    abstract fun getItemsOnList(listId: Long): kotlin.collections.List<ListCategoryItem>?

    @Query("SELECT * FROM ListSettings WHERE listId = :listId")
    abstract fun getListSettings(listId: Long): Flow<ListSettings>

    @Query("SELECT * FROM ListSettings WHERE listId = :listId")
    abstract suspend fun getListSetting(listId: Long): ListSettings?

    @Query("SELECT * FROM StoreCategoryCrossRef WHERE storeId = :storeId")
    abstract suspend fun getStore(storeId: Long):kotlin.collections.List<StoreCategoryCrossRef>

    @Query("SELECT * FROM List WHERE listId = :listId")
    abstract suspend fun getList(listId: Long): List

    //@Query("SELECT * FROM ListSettings WHERE isHomeScreen = ${true}")
    //abstract suspend fun getHomeScreen(): kotlin.collections.List<ListSettings>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(listSettings: ListSettings): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(list: List): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(item: Item): Long

    @Update
    abstract suspend fun update(item: Item)

    @Update
    abstract suspend fun update(listSettings: ListSettings)

    @Update
    abstract suspend fun updateList(list: List)

    @Delete
    abstract suspend fun deleteList(list: List)

    @Delete
    abstract suspend fun deleteItemFromList(listCategoryItem: ListCategoryItem)

    @Delete
    abstract suspend fun delete(listItems: kotlin.collections.List<ListCategoryItem>)

    @Delete
    abstract suspend fun delete(listCategoryItem: Array<ListCategoryItem>)

    @Delete
    abstract suspend fun delete(listSettings: ListSettings)

    @Transaction
    open suspend fun insertOrUpdate(listSettings: ListSettings) {
        if (insert(listSettings) == -1L)
            update(listSettings)
    }

    @Transaction
    open suspend fun insertOrUpdate(item: Item) {
        if (insert(item) == -1L)
            update(item)
    }

    @Transaction
    @Query("SELECT * FROM list WHERE list.listId = :listId")
    abstract fun getListData(listId: Long): Flow<ListWithData>

}