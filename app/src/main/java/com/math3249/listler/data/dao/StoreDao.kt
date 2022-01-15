package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.StoreCategoryWithCategoryName
import com.math3249.listler.model.StoreWithCategories
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Store
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StoreDao: BaseDao() {

    @Query("SELECT * FROM Store WHERE storeId = :storeId")
    abstract fun getStoreWithCategories(storeId: Long): Flow<StoreWithCategories>

    @Query("SELECT * FROM Store")
    abstract fun getStores(): Flow<List<Store>>

    @Query("SELECT StoreCategoryCrossRef.*, Category.name as categoryName " +
            "FROM StoreCategoryCrossRef INNER JOIN Category " +
            "ON StoreCategoryCrossRef.categoryId = Category.categoryId " +
            "WHERE StoreCategoryCrossRef.storeId = :storeId " +
            "ORDER BY StoreCategoryCrossRef.sortOrder ASC")
    abstract fun getStoreCategories(storeId: Long): Flow<List<StoreCategoryWithCategoryName>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(storeCategory: StoreCategoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(store: Store): Long

    @Transaction
    @Update
    abstract suspend fun update(storeCategories: List<StoreCategoryCrossRef>)

    @Delete
    abstract suspend fun delete(storeCategory: StoreCategoryCrossRef)


}