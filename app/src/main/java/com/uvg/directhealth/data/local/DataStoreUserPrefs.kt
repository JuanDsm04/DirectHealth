package com.uvg.directhealth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.uvg.directhealth.domain.UserPreferences
import com.uvg.directhealth.domain.model.Role
import kotlinx.coroutines.flow.first

class DataStoreUserPrefs(private val dataStore: DataStore<Preferences>): UserPreferences {
    private val roleKey = stringPreferencesKey("role")

    override suspend fun setRole(role: Role) {
        dataStore.edit { preferences ->
            preferences[roleKey] = role.toString()
        }
    }

    override suspend fun getValue(key: String?): String? {
        val preferencesKey = when (key) {
            "role" -> roleKey
            else -> null
        }

        preferencesKey?.let {
            val preferences = dataStore.data.first()
            return preferences[preferencesKey]
        }

        return ""
    }

}