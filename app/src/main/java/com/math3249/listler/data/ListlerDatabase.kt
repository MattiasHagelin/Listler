package com.math3249.listler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.math3249.listler.data.dao.*
import com.math3249.listler.model.crossref.ListCategoryCrossRef
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.entity.List
import com.math3249.listler.model.entity.Store
import com.math3249.listler.util.DATABASE_NAME

@Database(version = 10,
    entities = [
    Item::class,
    List::class,
    Category::class,
    Store::class,
    ListCategoryCrossRef::class,
    ListItemCrossRef::class,
    StoreCategoryCrossRef::class,
    ListCategoryItemCrossRef::class
               ],
    exportSchema = false
)
abstract class ListlerDatabase : RoomDatabase(){
    abstract fun itemDao(): ItemDao
    abstract fun listDao(): ListDao
    abstract fun listDetailDao(): ListDetailDao
    abstract fun categoryDao(): CategoryDao
    abstract fun storeDao(): StoreDao

    companion object{
        private var INSTANCE: ListlerDatabase? = null

        fun getDatabase(context: Context): ListlerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListlerDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}