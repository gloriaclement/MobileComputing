package com.gclem19.smartpantry.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Room Database: Create the database instance.

@Database(entities = [ShoppingList::class, PantryItem::class], version = 2, exportSchema = false) //,
abstract class SmartPantryDB : RoomDatabase() {
    abstract fun smartPantryDao(): SmartPantryDao
    //a companion object is similar to Java static declarations.
    //adding companion to the object declaration allows for adding
    // the "static" functionality to an object
    // used to create singleton object
    companion object {
        @Volatile
        private var INSTANCE: SmartPantryDB? = null
        fun getDatabase(context: Context): SmartPantryDB {
            //?: takes the right-hand value if the left-hand value is null (the elvis operator)
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmartPantryDB::class.java,
                    "smart_pantry_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}