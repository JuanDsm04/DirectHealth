package com.uvg.directhealth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.uvg.directhealth.domain.UserPreferences
import com.uvg.directhealth.domain.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreUserPrefs(private val dataStore: DataStore<Preferences>): UserPreferences {
    private val roleKey = stringPreferencesKey("role")
    private val userIdKey = stringPreferencesKey("userId")
    private val isAuthenticatedKey = booleanPreferencesKey("isAuthenticated")

    override suspend fun setUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[userIdKey] = id
        }
    }

    override suspend fun setRole(role: Role) {
        dataStore.edit { preferences ->
            preferences[roleKey] = role.toString()
        }
    }

    override suspend fun getValue(key: String?): String? {
        val preferencesKey = when (key) {
            "role" -> roleKey
            "userId" -> userIdKey
            else -> null
        }

        preferencesKey?.let {
            val preferences = dataStore.data.first()
            return preferences[preferencesKey]
        }

        return ""
    }

    override suspend fun logIn() {
        dataStore.edit { preferences ->
            preferences[isAuthenticatedKey] = true
        }
    }

    override suspend fun logOut() {
        dataStore.edit { preferences ->
            preferences[isAuthenticatedKey] = false
        }
    }

    override fun authStatus(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[isAuthenticatedKey] ?: false
    }
}