package com.gclem19.smartpantry.viewmodel

import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gclem19.smartpantry.MainActivity
import com.gclem19.smartpantry.data.PantryItem
import com.gclem19.smartpantry.data.PreferencesManager
import com.gclem19.smartpantry.data.ShoppingList
import com.gclem19.smartpantry.data.SmartPantryDB
import com.gclem19.smartpantry.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmartPantryViewModel(application: Application): AndroidViewModel(application) {

    private val smartPantryDao = SmartPantryDB.getDatabase(application).smartPantryDao()
    private val preferencesManager = PreferencesManager(application)

    private val _shoppingList = MutableStateFlow<List<ShoppingList>>(emptyList())
    val shoppingList = _shoppingList.asStateFlow()

    private val _pantryList = MutableStateFlow<List<PantryItem>>(emptyList())
    val pantryList = _pantryList.asStateFlow()

    private val _recipesForExpiringItems = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val recipesForExpiringItems = _recipesForExpiringItems.asStateFlow()

    private val _autofillItem = MutableStateFlow<ShoppingList?>(null)
    val autofillItem = _autofillItem.asStateFlow()

    private val _isUserRegistered = MutableStateFlow(false)
    val isUserRegistered: StateFlow<Boolean> = _isUserRegistered

    val username = preferencesManager.username
    val email = preferencesManager.email
    val phone = preferencesManager.phone
    val dob = preferencesManager.dob
    val isLoggedIn = preferencesManager.isLoggedIn.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        // Load initial data
        viewModelScope.launch {
            smartPantryDao.getAllShoppingItems().collectLatest { _shoppingList.value = it }
        }
        viewModelScope.launch {
            smartPantryDao.getAllPantryItems().collectLatest { _pantryList.value = it }
        }
        viewModelScope.launch {
            preferencesManager.isUserRegistered().collectLatest {
                _isUserRegistered.value = it
            }
        }
    }

    fun addItemShoppingList(item: ShoppingList) {
        viewModelScope.launch {
            smartPantryDao.insertShoppingItem(item)
        }
    }

    fun removeFromShoppingList(item: ShoppingList) {
        viewModelScope.launch {
            smartPantryDao.deleteShoppingItem(item)
        }
    }

    fun addItemPantryList(item: PantryItem) {
        viewModelScope.launch {
            smartPantryDao.insertPantryItem(item)
        }
    }

    fun removeFromPantryList(item: PantryItem) {
        viewModelScope.launch {
            smartPantryDao.deletePantryItem(item)
        }
    }

    fun addItemToPantryList(item: ShoppingList) {
        viewModelScope.launch {
            val pantryItem = PantryItem(
                name = item.name,
                quantity = item.quantity,
                category = item.category,
                date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            )
            smartPantryDao.insertPantryItem(pantryItem)
        }
    }

    fun setItemToAutofill(item: ShoppingList) {
        _autofillItem.value = item
    }

    fun clearAutofill() {
        _autofillItem.value = null
    }

    fun getExpiringItems(): List<PantryItem> {
        val expiringItems = mutableListOf<PantryItem>()
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        _pantryList.value.forEach { item ->
            try {
                val expiryDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(item.date)
                val current = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(currentDate)
                val diff = expiryDate.time - current.time
                val daysUntilExpiration = diff / (1000 * 60 * 60 * 24)
                if (daysUntilExpiration in 0..3) {
                    expiringItems.add(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return expiringItems
    }

    fun getRecipesForIngredient(ingredient: String, onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getMealsByIngredient(ingredient)
            }
            val meals = response.meals?.map { it.strMeal } ?: emptyList()
            onResult(meals)
        }
    }

    fun fetchRecipeSuggestionsForExpiringItems(expiringItems: List<PantryItem>) {
        expiringItems.forEach { item ->
            getRecipesForIngredient(item.name) { recipes ->
                val currentMap = _recipesForExpiringItems.value.toMutableMap()
                currentMap[item.name] = recipes
                _recipesForExpiringItems.value = currentMap
            }
        }
    }

    fun showExpiryNotification(context: Context, item: String, recipes: List<String>) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val recipeSuggestions = recipes.joinToString("\n")

        val notification = NotificationCompat.Builder(context, "expiry_channel")
            .setSmallIcon(android.R.drawable.stat_notify_sync_noanim)
            .setContentTitle("Expiring Soon: $item")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Recipes:\n$recipeSuggestions"))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(item.hashCode(), notification)
    }

    fun checkAndNotifyExpiringItems(context: Context) {
        val expiringItems = getExpiringItems()
        fetchRecipeSuggestionsForExpiringItems(expiringItems)

        expiringItems.forEach { item ->
            val recipes = _recipesForExpiringItems.value[item.name] ?: emptyList()
            showExpiryNotification(context, item.name, recipes)
        }
    }

    // Register user
    fun register(
        username: String,
        email: String,
        phone: String,
        dob: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            preferencesManager.register(username, email, phone, dob, password)
            onSuccess()
        }
    }

    // Login with credential validation
    fun login(username: String, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val isValid = preferencesManager.login(username, password)
            if (isValid) {
                onSuccess()
            } else {
                onError()
            }
        }
    }
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            preferencesManager.logout()
            onSuccess()
        }
    }

}
