package com.math3249.listler.data.dao

//import com.math3249.listler.model.crossref.ListCategory
//mport com.math3249.listler.model.crossref.ListItem
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.crossref.ListCategoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ListDetailDao: BaseDao() {

    @Query ("SELECT EXISTS (SELECT 1 FROM item WHERE item.name = :name)")
    abstract fun itemExists(name: String): Boolean

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId")
    abstract fun getListItems(listId: Long): Flow<List<ListCategoryItem>>

    @Query("SELECT * FROM ListCategoryItem WHERE listId = :listId AND itemName = :itemName")
    abstract fun getListItem(listId: Long, itemName: String): ListCategoryItem?
/*
    @Query("SELECT EXISTS (SELECT 1 FROM ListItem WHERE name = :itemName)")
    abstract fun itemExistsInList(itemName: String): Boolean

    @Query("SELECT * FROM ListItem WHERE listId = :listId")
    abstract fun getListItem(listId: Long): Flow<List<ListItem>>

    @Query("SELECT * FROM ListItem WHERE listId = :listId")
    abstract fun getListItems(listId: Long): List<ListItem>

    @Query("SELECT * FROM ListItem WHERE listId = :listId AND itemId = :itemId")
    abstract fun getListItem(listId: Long, itemId: Long): ListItem?

    @Query("SELECT * FROM ListItem WHERE name = :itemName")
    abstract fun getListItemByName(itemName: String): ListItem?
*/
    @Transaction
    @Query("SELECT * FROM list WHERE list.listId = :listId")
    abstract fun getListData(listId: Long): Flow<ListWithData>

    //@Query("SELECT Category.name FROM ListCategory " +
    //        "INNER JOIN Category ON ListCategory.categoryId = Category.categoryId " +
    //        "WHERE ListCategory.listId = :listId")
    //abstract fun getCategories(listId: Long): List<String>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    abstract suspend fun insert(listCategory: ListCategory): Long

    @Delete
    abstract suspend fun deleteItemFromList(listCategoryItem: ListCategoryItem)

    @Delete
    abstract suspend fun delete(listItems: List<ListCategoryItem>)

/*    @Delete
    abstract suspend fun delete(listItem: ListItem): Int

    @Delete
    abstract suspend fun delete(listCategory: ListCategory)
*/
}