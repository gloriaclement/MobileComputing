package com.gclem19.smartpantry.data

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val USER_NAME_KEY = stringPreferencesKey("username")
    val EMAIL_KEY = stringPreferencesKey("email")
    val PHONE_KEY = stringPreferencesKey("phone")
    val DOB_KEY = stringPreferencesKey("dob")
    val PASSWORD_KEY = stringPreferencesKey("password")
    val LOGGED_IN_KEY = stringPreferencesKey("logged_in")
}