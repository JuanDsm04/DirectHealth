package com.uvg.directhealth.layouts.mainFlow.appointment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.source.AppointmentDb
import com.uvg.directhealth.data.source.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppointmentListViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    private val appointmentList = savedStateHandle.toRoute<AppointmentListDestination>()
    private val _state = MutableStateFlow(AppointmentListState())
    val state = _state.asStateFlow()

    private val userDb = UserDb()
    private val appointmentDb = AppointmentDb()

    fun onEvent(event: AppointmentListEvent) {
        when (event) {
            AppointmentListEvent.PopulateData -> getData()
        }
    }

    private fun getData() {
        val user = userDb.getUserById(appointmentList.userId)

        _state.update { state ->
            if (user.role == Role.DOCTOR) {
                state.copy(
                    appointmentList = appointmentDb.getAppointmentsByDoctorId(user.id),
                    role = Role.DOCTOR
                )

            } else {
                state.copy(
                    appointmentList = appointmentDb.getAppointmentsByPatientId(user.id),
                    role = Role.PATIENT
                )
            }
        }
    }
}