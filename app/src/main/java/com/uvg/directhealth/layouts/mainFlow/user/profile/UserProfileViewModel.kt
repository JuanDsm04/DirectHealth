package com.uvg.directhealth.layouts.mainFlow.user.profile

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.AppointmentDto
import com.uvg.directhealth.data.network.dto.toUser
import com.uvg.directhealth.data.network.repository.AppointmentRepositoryImpl
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.domain.repository.AppointmentRepository
import com.uvg.directhealth.domain.repository.UserRepository
import com.uvg.directhealth.util.Result
import com.uvg.directhealth.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class UserProfileViewModel(
    private val userRepository: UserRepository,
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.PopulateData -> populateData(event.loggedUserId, event.userProfileId)
            is UserProfileEvent.UpdateSelectedDate -> _state.update { it.copy(selectedDate = event.date) }
            is UserProfileEvent.UpdateSelectedTime -> _state.update { it.copy(selectedTime = event.time) }
            is UserProfileEvent.ToggleDatePicker -> {
                _state.update { it.copy(showDatePicker = event.show) }
            }
            is UserProfileEvent.ToggleTimePicker -> {
                _state.update { it.copy(showTimePicker = event.show) }
            }

            is UserProfileEvent.ScheduleAppointment -> scheduleAppointment(event.userID)

            UserProfileEvent.ResetSuccessfulCreateAppointment -> _state.update { it.copy(successfulCreateAppointment = false) }
        }
    }

    private fun populateData(loggedUserId: String, profileUserId: String) {
        viewModelScope.launch {
            val loggedUserResult = userRepository.getUserById(loggedUserId)
            val profileUserResult = userRepository.getUserById(profileUserId)

            if (loggedUserResult is Result.Success && profileUserResult is Result.Success) {
                val loggedUser = loggedUserResult.data.toUser()
                val userProfile = profileUserResult.data.toUser()

                _state.update { state ->
                    state.copy(
                        loggedUser = loggedUser,
                        userProfile = userProfile,
                        isLoading = false,
                        hasError = false
                    )
                }
            } else {
                _state.update {
                    it.copy(isLoading = false, hasError = true)
                }
            }
        }
    }

    private fun scheduleAppointment(userId: String) {
        viewModelScope.launch {
            val patient: User = _state.value.loggedUser!!

            val input = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val output = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())

            val date = _state.value.selectedDate
            val time = _state.value.selectedTime

            appointmentRepository
                .addAppointment(
                    AppointmentDto(
                        doctorId = userId,
                        patientId = patient.id,
                        date = output.format(input.parse("$date $time"))
                    )
                )
                .onSuccess {
                    _state.update { state ->
                        state.copy(
                            selectedDate = "dd/mm/yyyy",
                            selectedTime = "00:00",
                            showDatePicker = false,
                            showTimePicker = false,
                            initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                            initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
                            successfulCreateAppointment = true
                        )
                    }
                }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val userRepository = UserRepositoryImpl(api)

                UserProfileViewModel(
                    userRepository = userRepository,
                    appointmentRepository = AppointmentRepositoryImpl(api)
                )
            }
        }
    }
}