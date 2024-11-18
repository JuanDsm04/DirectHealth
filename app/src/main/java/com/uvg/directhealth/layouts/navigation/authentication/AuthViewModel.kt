package com.uvg.directhealth.layouts.navigation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.dataStore
import com.uvg.directhealth.domain.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel (
    private val preferences: UserPreferences
): ViewModel() {
    val authStatus = preferences.authStatus()
        .map { isLogged ->
            if (isLogged) {
                AuthState.Authenticated
            } else {
                AuthState.NonAuthenticated
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AuthState.Loading
        )
    fun login() {
        viewModelScope.launch {
            preferences.logIn()
        }
    }
    fun logout() {
        viewModelScope.launch {
            preferences.logOut()
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                AuthViewModel(
                    preferences = DataStoreUserPrefs(application.dataStore)
                )
            }
        }
    }
}