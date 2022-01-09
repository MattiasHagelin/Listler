package com.math3249.listler.data.dao

import androidx.room.*
import com.math3249.listler.model.StoreWithCategories
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Store
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StoreDao {

    @Query("SELECT * FROM Store WHERE storeId = :storeId")
    abstract fun getStoreWithCategories(storeId: Long): Flow<StoreWithCategories>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(storeCategory: StoreCategoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(store: Store): Long

    @Update
    abstract suspend fun update(storeCategory: StoreCategoryCrossRef)

    @Delete
    abstract suspend fun delete(storeCategory: StoreCategoryCrossRef)


}