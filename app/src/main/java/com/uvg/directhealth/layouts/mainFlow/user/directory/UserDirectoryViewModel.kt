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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.uvg.directhealth.data.network.dto.toUser
import com.uvg.directhealth.data.network.repository.AppointmentRepositoryImpl
import com.uvg.directhealth.domain.repository.AppointmentRepository

import com.uvg.directhealth.util.Result

class UserDirectoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val userPreferences: UserPreferences,
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository

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

            when (val allUsersResult = userRepository.getAllUsers()) {
                is Result.Success -> {
                    val allUsers = allUsersResult.data.map { it.toUser() }
                    val loggedUser = allUsers.find { it.id == userId }

                    if (loggedUser != null) {
                        if (loggedUser.role == Role.DOCTOR) {
                            handleDoctorDirectory(loggedUser, allUsers)
                        } else {
                            handlePatientDirectory(loggedUser, allUsers)
                        }
                    } else {
                        handleError()
                    }
                }

                is Result.Error -> handleError()
            }
        }
    }

    private suspend fun handleDoctorDirectory(loggedUser: User, allUsers: List<User>) {
        when (val appointmentsResult = appointmentRepository.getAllAppointments(loggedUser.id)) {
            is Result.Success -> {
                val patientIds = appointmentsResult.data.map { it.patientId }.toSet()
                val filteredUsers = allUsers.filter {
                    it.role == Role.PATIENT && patientIds.contains(it.id)
                }

                _state.update { state ->
                    state.copy(
                        userName = loggedUser.name,
                        userRole = Role.DOCTOR,
                        userList = filteredUsers,
                        isLoading = false,
                        hasError = false
                    )
                }
            }

            is Result.Error -> handleError()
        }
    }

    private fun handlePatientDirectory(loggedUser: User, allUsers: List<User>) {
        val filteredUsers = allUsers.filter { it.role == Role.DOCTOR }

        _state.update { state ->
            state.copy(
                userName = loggedUser.name,
                userRole = Role.PATIENT,
                userList = filteredUsers,
                isLoading = false,
                hasError = false
            )
        }
    }

    private fun handleError() {
        _state.update {
            it.copy(
                isLoading = false,
                hasError = true
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val userRepository = UserRepositoryImpl(api)
                val appointmentRepository = AppointmentRepositoryImpl(api)

                UserDirectoryViewModel(
                    userPreferences = DataStoreUserPrefs(application.dataStore),
                    savedStateHandle = createSavedStateHandle(),
                    userRepository = userRepository,
                    appointmentRepository = appointmentRepository
                )
            }
        }
    }
}