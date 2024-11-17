package com.uvg.directhealth.layouts.mainFlow.appointment

import com.uvg.directhealth.data.model.Appointment
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.User

data class AppointmentListState(
    val appointmentList: List<Appointment> = emptyList(),
    val userDetails: List<User> = emptyList(),
    val role: Role? = null,
    val isLoading: Boolean = true
)