package com.gclem19.smartpantry.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Create a data class representing a table in the database.


@Entity(tableName = "shopping_list")
data class ShoppingList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val quantity: String,
)

@Entity(tableName = "pantry_list")
data class PantryList(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val quantity: String,
    val date: String
)

//@Entity(tableName = "user_table") // modify "user_table" to the table name for your database
//data class User(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
//    val username: String,
//    val level: String = "1",
//    val score: Int = 0,
//    val duration: Int = 0,
//    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP") val date: Long = System.currentTimeMillis()
//
//)
