package com.gclem19.smartpantry.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gclem19.smartpantry.data.PantryItem
//import com.gclem19.smartpantry.data.PantryList
import com.gclem19.smartpantry.data.ShoppingList
import com.gclem19.smartpantry.data.SmartPantryDB
import com.gclem19.smartpantry.data.SmartPantryDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SmartPantryViewModel(application: Application): AndroidViewModel(application) {
    private val smartPantryDao: SmartPantryDao =
        SmartPantryDB.getDatabase(application).smartPantryDao()

    private val _shoppingList = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingList = _shoppingList.asStateFlow()

    private val _pantryList = MutableStateFlow<List<PantryItem>>(emptyList())
    val pantryList = _pantryList.asStateFlow()

    //retrive all users
    //val allUsers: List<User> = smartPantryDao.getAllUsers()
    //variables from user input should be defined here

    init {
        viewModelScope.launch {
            smartPantryDao.getAllShoppingItems().collectLatest { _shoppingList.value = it }
        }
        viewModelScope.launch {
            smartPantryDao.getAllPantryItems().collectLatest { _pantryList.value = it }
        }
    }

    fun addItemShoppingList(item: ShoppingList) {
        viewModelScope.launch {
            smartPantryDao.insertShoppingItem(item)
        }
    }

    fun addItemPantryList(item: PantryItem) {
        viewModelScope.launch {
            smartPantryDao.insertPantryItem(item)
        }
    }
}

//    fun insert(user: User) = viewModelScope.launch {
//        smartPantryDao.insert(user)
//    }
