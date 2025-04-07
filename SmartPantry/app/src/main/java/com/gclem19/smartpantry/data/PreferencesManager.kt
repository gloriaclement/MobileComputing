package com.gclem19.smartpantry.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to get the DataStore instance
val Context.dataStore by preferencesDataStore(name = "user_preferences")

class PreferencesManager(context: Context) {

    private val dataStore = context.dataStore

    // Keys
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
    val password: Flow<String?> = dataStore.data.map { it[PASSWORD] }
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[IS_LOGGED_IN] ?: false }

    // Register and auto-login
    suspend fun register(
        username: String,
        email: String,
        phone: String,
        dob: String,
        password: String
    ) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[EMAIL] = email
            preferences[PHONE] = phone
            preferences[DOB] = dob
            preferences[PASSWORD] = password
            preferences[IS_LOGGED_IN] = true
        }
    }

    // Login
    suspend fun loginUser(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[IS_LOGGED_IN] = true
        }
    }

    // Manual login status update
    suspend fun setUserLoggedIn(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = loggedIn
        }
    }

    // Logout and clear data
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences.remove(USERNAME)
            preferences.remove(EMAIL)
            preferences.remove(PHONE)
            preferences.remove(DOB)
            preferences.remove(PASSWORD)
        }
    }
}
