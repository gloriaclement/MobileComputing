package com.gclem19.smartpantry.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extension property to get the DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_preferences")


class PreferencesManager(context: Context) {

    private val dataStore = context.dataStore


    // Preference Keys
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USERNAME = stringPreferencesKey("username")
    private val EMAIL = stringPreferencesKey("email")
    private val PHONE = stringPreferencesKey("phone")
    private val DOB = stringPreferencesKey("dob")
    private val PASSWORD = stringPreferencesKey("password")

    // Getters
    val username: Flow<String?> = dataStore.data.map { it[USERNAME] }
    val email: Flow<String?> = dataStore.data.map { it[EMAIL] }
    val phone: Flow<String?> = dataStore.data.map { it[PHONE] }
    val dob: Flow<String?> = dataStore.data.map { it[DOB] }
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[IS_LOGGED_IN] ?: false }

    // Check if user is registered
    fun isUserRegistered(): Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[USERNAME] != null && prefs[PASSWORD] != null
        }

    // Register (does NOT auto-login for better logic)
    suspend fun register(
        username: String,
        email: String,
        phone: String,
        dob: String,
        password: String
    ) {
        dataStore.edit { prefs ->
            prefs[USERNAME] = username
            prefs[EMAIL] = email
            prefs[PHONE] = phone
            prefs[DOB] = dob
            prefs[PASSWORD] = password
            prefs[IS_LOGGED_IN] = false // Set false until login
        }
        println("Registered Username: $username, Password: $password") // For debugging
    }

    // Login with validation
    suspend fun login(username: String, password: String): Boolean {
        val prefs = dataStore.data.first() // Fetch the data from DataStore
        val savedUsername = prefs[USERNAME] // Get the saved username
        val savedPassword = prefs[PASSWORD] // Get the saved password

        println("Retrieved Username: $savedUsername, Retrieved Password: $savedPassword") // For debugging

        return if (username == savedUsername && password == savedPassword) {
            dataStore.edit { it[IS_LOGGED_IN] = true }
            true
        } else {
            false
        }
    }

    // Manual login state setter (optional)
    suspend fun setUserLoggedIn(loggedIn: Boolean) {
        dataStore.edit { it[IS_LOGGED_IN] = loggedIn }
    }

    // Logout
    suspend fun logout() {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = false
        }
    }
}