package com.gclem19.smartpantry.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SmartPantryDao {
    //suspending function: a function that can be paused and resumed at a later time
    //suspending function: can execute a long running operation and wait for it to complete without blocking
    //suspending functions can only be invoked by another suspending function or within a coroutine
    //Use suspend:
    //1. When defining asynchronous functions.
    //2. For IO-bound operations (network, database, file).
    //3. To declare functions that can be paused and resumed.
    // Get all items from the shopping_list table


// Insert a shopping list item, replace if already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingList: ShoppingList)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPantryItem(pantryList: PantryList)

    @Query("SELECT * FROM shopping_list")
    fun getAllShoppingItems(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM pantry_list ORDER BY id DESC")
    fun getAllPantryItems(): Flow<List<PantryList>>



//    // Insert a user
//    @Insert
//    suspend fun insert(user: User)
//    // Get a user by ID
//    @Query("SELECT * FROM user_table WHERE id = :id")
//    suspend fun getUserById(id: Int): User?
//    // Delete all users
//    @Query("DELETE FROM user_table")
//    suspend fun deleteAllUsers()
//
//    // Get all users ordered by level, score, duration, and username
//    @Query("SELECT * FROM user_table ORDER BY level ASC, score DESC, duration ASC, username ASC")
//    fun getAllUsers(): List<User>
//
//    // Get all users live data
//    @Query("SELECT * FROM user_table ORDER BY level ASC, score DESC, duration ASC, username ASC")
//    fun getAllUsersLiveData(): LiveData<List<User>>

}


