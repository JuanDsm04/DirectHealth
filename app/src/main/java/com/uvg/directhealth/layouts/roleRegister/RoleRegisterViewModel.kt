package com.uvg.directhealth.layouts.roleRegister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.dataStore
import com.uvg.directhealth.domain.UserPreferences
import com.uvg.directhealth.domain.model.Role
import kotlinx.coroutines.launch

class RoleRegisterViewModel(private val userPreferences: UserPreferences): ViewModel() {
    fun saveRole(role: Role) {
        viewModelScope.launch {
            userPreferences.setRole(role)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])

                RoleRegisterViewModel(
                    userPreferences = DataStoreUserPrefs(application.dataStore)
                )
            }
        }
    }
}