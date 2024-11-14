package com.uvg.directhealth.layouts.mainFlow.appointment

import com.uvg.directhealth.data.model.Appointment
import com.uvg.directhealth.domain.model.Role

data class AppointmentListState(
    val appointmentList: List<Appointment> = emptyList(),
    val role: Role? = null
)