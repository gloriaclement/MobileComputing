package com.gclem19.smartpantry.viewmodel

//import com.gclem19.smartpantry.data.PantryList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gclem19.smartpantry.data.PantryItem
import com.gclem19.smartpantry.data.ShoppingList
import com.gclem19.smartpantry.data.SmartPantryDB
import com.gclem19.smartpantry.data.SmartPantryDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmartPantryViewModel(application: Application): AndroidViewModel(application) {
    private val smartPantryDao: SmartPantryDao =
        SmartPantryDB.getDatabase(application).smartPantryDao()

    private val _shoppingList = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingList = _shoppingList.asStateFlow()

    private val _pantryList = MutableStateFlow<List<PantryItem>>(emptyList())
    val pantryList = _pantryList.asStateFlow()

    // Initialize the pantry items
    init {
        viewModelScope.launch {
            smartPantryDao.getAllShoppingItems().collectLatest { _shoppingList.value = it }
        }
        viewModelScope.launch {
            smartPantryDao.getAllPantryItems().collectLatest { _pantryList.value = it }
        }
    }

    // Add item to shopping list
    fun addItemShoppingList(item: ShoppingList) {
        viewModelScope.launch {
            smartPantryDao.insertShoppingItem(item)
        }
    }

    // Add item to pantry list
    fun addItemPantryList(item: PantryItem) {
        viewModelScope.launch {
            smartPantryDao.insertPantryItem(item)
        }
    }

    // Function to get items that are expiring soon (within 3 days)
    fun getExpiringItems(): List<PantryItem> {
        val expiringItems = mutableListOf<PantryItem>()

        // Get current date
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        // Loop through the pantry list and check expiration
        _pantryList.value.forEach { item ->
            try {
                val expiryDate =
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(item.date)
                val current = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(currentDate)
                val diff = expiryDate.time - current.time
                val daysUntilExpiration = diff / (1000 * 60 * 60 * 24)

                // If the item expires in 3 days or less
                if (daysUntilExpiration in 0..3) {
                    expiringItems.add(item)
                }
            } catch (e: Exception) {
                // Handle date parsing error if any
                e.printStackTrace()
            }
        }

        return expiringItems
    }
}

//    fun insert(user: User) = viewModelScope.launch {
//        smartPantryDao.insert(user)
//    }
