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
import com.gclem19.smartpantry.data.SmartPantryDao
import com.gclem19.smartpantry.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _recipesForExpiringItems = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val recipesForExpiringItems = _recipesForExpiringItems.asStateFlow()

    private val _autofillItem = MutableStateFlow<ShoppingList?>(null)
    val autofillItem = _autofillItem.asStateFlow()

    private val _itemToAutofill = MutableStateFlow<ShoppingList?>(null)
//    val itemToAutofill: StateFlow<ShoppingList?> = _itemToAutofill

    private val preferencesManager = PreferencesManager(application)
    // Expose data from PreferencesManager
    // Expose registration data
    val username = preferencesManager.username
    val email = preferencesManager.email
    val phone = preferencesManager.phone
    val dob = preferencesManager.dob
    val isLoggedIn = preferencesManager.isLoggedIn.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )


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

    fun getRecipesForIngredient(ingredient: String, onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getMealsByIngredient(ingredient)
            }
            val meals = response.meals?.map { it.strMeal } ?: emptyList()
            onResult(meals)
        }
    }

    // Function to fetch recipe suggestions for each expiring item
    fun fetchRecipeSuggestionsForExpiringItems(expiringItems: List<PantryItem>) {
        expiringItems.forEach { item ->
            getRecipesForIngredient(item.name) { recipes ->
                val currentMap = _recipesForExpiringItems.value.toMutableMap()
                currentMap[item.name] = recipes
                _recipesForExpiringItems.value = currentMap
            }
        }
    }

    fun showExpiryNotification(
        context: Context,
        item: String,
        recipes: List<String>
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

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
        fetchRecipeSuggestionsForExpiringItems(expiringItems) // Fetch recipes for expiring items

        // Notify about each expiring item
        expiringItems.forEach { item ->
            val recipes = _recipesForExpiringItems.value[item.name] ?: emptyList()
            showExpiryNotification(context, item.name, recipes)
        }
    }


    fun removeFromShoppingList(item: ShoppingList) {
        viewModelScope.launch {
            smartPantryDao.deleteShoppingItem(item)  // remove from DB
        }
    }
    fun removeFromPantryList(item: PantryItem) {
        viewModelScope.launch {
            smartPantryDao.deletePantryItem(item)  // remove from DB
        }
    }

    fun addItemToPantryList(item: ShoppingList) {
        viewModelScope.launch {
            // Create a PantryItem object from ShoppingList
            val pantryItem = PantryItem(
                name = item.name,
                quantity = item.quantity,
                category = item.category,
                date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) // Use current date
            )
            smartPantryDao.insertPantryItem(pantryItem) // Add to pantry list in DB
        }
    }

    fun setItemToAutofill(item: ShoppingList) {
        _autofillItem.value = item
    }

    fun clearAutofill() {
        _itemToAutofill.value = null
    }

    // Register method
    // Register user in DataStore
    fun register(username: String, email: String, phone: String, dob: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            preferencesManager.register(username, email, phone, dob, password)
            onSuccess() // Notify on success
        }
    }

    // Logout method
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            preferencesManager.logout()
            onSuccess()
        }
    }

    // Login method (You can add logic to check if the username is valid or check if the user is already logged in)
    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            preferencesManager.setUserLoggedIn(true)
            onSuccess() // Notify on success
        }
    }

    fun saveUserLogin(username: String) {
        viewModelScope.launch {
            preferencesManager.loginUser(username)
        }
    }
}



//    fun insert(user: User) = viewModelScope.launch {
//        smartPantryDao.insert(user)
//    }
