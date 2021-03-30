package com.mertgolcu.basicnote.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class TokenPreferences(val token: String)

private const val TAG = "PreferencesManager"

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_preferences")

    val tokenPreferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences ", exception)
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

    private object PreferencesKeys {
        val TOKEN = preferencesKey<String>("token")
    }
}