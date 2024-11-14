package com.uvg.directhealth.layouts.mainFlow.user.directory

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.repository.AuthRepositoryImpl
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.dataStore
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.UserPreferences
import com.uvg.directhealth.layouts.login.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDirectoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences
): ViewModel() {
    private val userList = savedStateHandle.toRoute<UserDirectoryDestination>()
    private val _state = MutableStateFlow(UserDirectoryState())
    val state = _state.asStateFlow()

    private val userDb = UserDb()

    fun onEvent(event: UserDirectoryEvent) {
        when (event) {
            UserDirectoryEvent.PopulateData -> getData()
        }
    }

    private fun getData() {
        val loggedUser = userDb.getUserById(userList.userId)

        _state.update { state ->
            if (loggedUser.role == Role.DOCTOR) {
                state.copy(
                    userName = loggedUser.name,
                    userRole = loggedUser.role,
                    userList = userDb.getAllPatients()
                )
            } else {
                state.copy(
                    userName = loggedUser.name,
                    userRole = loggedUser.role,
                    userList = userDb.getAllDoctors()
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])

                UserDirectoryViewModel(
                    userPreferences = DataStoreUserPrefs(application.dataStore),
                    savedStateHandle = createSavedStateHandle()
                )
            }
        }
    }
}