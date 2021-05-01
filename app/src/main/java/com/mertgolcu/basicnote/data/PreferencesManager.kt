package com.mertgolcu.basicnote.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class TokenPreferences(val token: String)

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.createDataStore("user_preferences")

    private val tokenPreferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                //Log.e(TAG, "Error reading preferences ", exception)
                emit(emptyPreferences())
            } else
                throw exception
        }
        .map { preferences ->
            val token = preferences[PreferencesKeys.TOKEN] ?: ""
            TokenPreferences(token)
        }

    suspend fun updateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TOKEN] = token
        }
    }

    suspend fun getToken(): String {
        return tokenPreferencesFlow.first().token
    }

    private object PreferencesKeys {
        val TOKEN = preferencesKey<String>("token")
    }
}