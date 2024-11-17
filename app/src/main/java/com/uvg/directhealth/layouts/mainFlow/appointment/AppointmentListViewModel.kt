package com.uvg.directhealth.layouts.mainFlow.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.data.model.Appointment
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.toAppointment
import com.uvg.directhealth.data.network.dto.toUser
import com.uvg.directhealth.data.network.repository.AppointmentRepositoryImpl
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.dataStore
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppointmentListViewModel(
    private val userPrefs: DataStoreUserPrefs,
    private val appointmentRepository: AppointmentRepositoryImpl,
    private val userRepositoryImpl: UserRepositoryImpl

) : ViewModel() {
    private val _state = MutableStateFlow(AppointmentListState())
    val state = _state.asStateFlow()

    init {
        getData()
    }

    fun onEvent(event: AppointmentListEvent) {
        when (event) {
            AppointmentListEvent.PopulateData -> getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            val userId = userPrefs.getValue("userId")
            val roleString = userPrefs.getValue("role")
            val role = roleString?.let { Role.valueOf(it) }

            if (userId != null && role != null) {
                val result = appointmentRepository.getAllAppointments(userId)

                _state.update { state ->
                    if (result is Result.Success) {
                        val appointmentList = result.data.map { it.toAppointment() }
                        val usersResult = getUsersDetails(appointmentList)

                        state.copy(
                            appointmentList = appointmentList,
                            userDetails = usersResult,
                            role = role,
                            isLoading = false
                        )
                    }  else {
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    private suspend fun getUsersDetails(appointments: List<Appointment>): List<User> {
        val userIds = appointments.flatMap { listOf(it.doctorId, it.patientId) }.distinct()
        val users = mutableListOf<User>()

        for (userId in userIds) {
            val result = userRepositoryImpl.getUserById(userId)
            if (result is Result.Success) {
                result.data.toUser().also { users.add(it) }
            }
        }
        return users
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val appointmentRepository = AppointmentRepositoryImpl(api)
                val userRepository = UserRepositoryImpl(api)

                AppointmentListViewModel(
                    userPrefs = DataStoreUserPrefs(application.dataStore),
                    appointmentRepository = appointmentRepository,
                    userRepositoryImpl = userRepository
                )
            }
        }
    }
}