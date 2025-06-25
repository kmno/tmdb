package com.kmno.tmdb.utils

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Kamran Nourinezhad on 24 June-6 2025.
 * Copyright (c)  2025 MCI.
 */
class UserPreferences @Inject constructor(@ApplicationContext context: Context) {
    val dataStore = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("user_prefs")
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    val authToken: Flow<String?> = dataStore.data
        .map { it[TOKEN_KEY] }

    suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }

    /*var isDarkModeEnabled: Boolean
        get() = sharedPreferences.getBoolean("dark_mode", false)
        set(value) {
            sharedPreferences.edit().putBoolean("dark_mode", value).apply()
        }

    var isNotificationsEnabled: Boolean
        get() = sharedPreferences.getBoolean("notifications", true)
        set(value) {
            sharedPreferences.edit().putBoolean("notifications", value).apply()
        }*/
}