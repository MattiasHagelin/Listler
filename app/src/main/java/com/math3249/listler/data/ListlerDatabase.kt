package com.math3249.listler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.math3249.listler.data.dao.CategoryDao
import com.math3249.listler.data.dao.ItemDao
import com.math3249.listler.data.dao.ListDao
import com.math3249.listler.data.dao.StoreDao
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.*
import com.math3249.listler.model.entity.List
import com.math3249.listler.util.DATABASE_NAME

@Database(version = 21,
    entities = [
    Item::class,
    List::class,
    Category::class,
    Store::class,
    ListSettings::class,
    StoreCategoryCrossRef::class,
    ListCategoryItem::class
               ],
    exportSchema = true
)
abstract class ListlerDatabase : RoomDatabase(){
    abstract fun itemDao(): ItemDao
    abstract fun listDao(): ListDao
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
                    .createFromAsset("default_v21.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}