package com.gclem19.smartpantry.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gclem19.smartpantry.data.SmartPantryDB
import com.gclem19.smartpantry.data.SmartPantryDao
import com.gclem19.smartpantry.data.User
import kotlinx.coroutines.launch

class SmartPantryViewModel(application: Application): AndroidViewModel(application) {
    private val smartPantryDao:SmartPantryDao =
        SmartPantryDB.getDatabase(application).smartPantryDao()
    //retrive all users
    val allUsers: List<User> = smartPantryDao.getAllUsers()
    //variables from user input should be defined here

    fun insert(user: User) = viewModelScope.launch {
        smartPantryDao.insert(user)
    }
}