package com.uvg.directhealth.layouts.mainFlow.user.directory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.dataStore
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.UserPreferences
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.domain.repository.UserRepository
import com.uvg.directhealth.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.uvg.directhealth.data.network.dto.toUser

class UserDirectoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences,
    private val userRepository: UserRepository

): ViewModel() {
    private val _state = MutableStateFlow(UserDirectoryState())
    val state = _state.asStateFlow()

    init {
        getData()
    }

    fun onEvent(event: UserDirectoryEvent) {
        when (event) {
            UserDirectoryEvent.PopulateData -> getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            val userId = userPreferences.getValue("userId")

            userRepository.getAllUsers().onSuccess { allUserDtos ->
                val allUsers = allUserDtos.map { it.toUser() }
                val loggedUser = allUsers.find { it.id == userId }

                if (loggedUser != null) {
                    val filteredUsers: List<User>
                    val role: Role

                    if (loggedUser.role == Role.DOCTOR) {
                        filteredUsers = allUsers.filter { it.role == Role.PATIENT }
                        role = Role.DOCTOR
                    } else {
                        filteredUsers = allUsers.filter { it.role == Role.DOCTOR }
                        role = Role.PATIENT
                    }

                    _state.update { state ->
                        state.copy(
                            userName = loggedUser.name,
                            userRole = role,
                            userList = filteredUsers,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val userRepository = UserRepositoryImpl(api)

                UserDirectoryViewModel(
                    userPreferences = DataStoreUserPrefs(application.dataStore),
                    savedStateHandle = createSavedStateHandle(),
                    userRepository = userRepository
                )
            }
        }
    }
}