package com.math3249.listler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.math3249.listler.data.dao.CategoryDao
import com.math3249.listler.data.dao.ItemDao
import com.math3249.listler.data.dao.ListDao
import com.math3249.listler.data.dao.ListDetailDao
import com.math3249.listler.model.crossref.CategoryItemCrossRef
import com.math3249.listler.model.crossref.ListItemCrossRef
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import com.math3249.listler.model.entity.List
import com.math3249.listler.model.entity.Store

@Database(version = 4,
    entities = [
    Item::class,
    List::class,
    ListItemCrossRef::class,
    Category::class,
    Store::class,
    StoreCategoryCrossRef::class
               ],
    exportSchema = false
)
abstract class ListlerDatabase : RoomDatabase(){
    abstract fun itemDao(): ItemDao
    abstract fun listDao(): ListDao
    abstract fun listDetailDao(): ListDetailDao
    abstract fun categoryDao(): CategoryDao

    companion object{
        private var INSTANCE: ListlerDatabase? = null

        fun getDatabase(context: Context): ListlerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListlerDatabase::class.java,
                    "listler_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}