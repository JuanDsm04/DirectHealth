package com.uvg.directhealth.data.network.dto

import com.uvg.directhealth.data.model.Appointment
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class AppointmentDto(
    val id: String? = null,
    val doctorId: String,
    val patientId: String,
    val date: String
)

fun AppointmentDto.toAppointment(): Appointment {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val parsedDate = LocalDateTime.parse(this.date, formatter)

    return Appointment(
        id = this.id ?: "",
        doctorId = this.doctorId,
        patientId = this.patientId,
        date = parsedDate
    )
}