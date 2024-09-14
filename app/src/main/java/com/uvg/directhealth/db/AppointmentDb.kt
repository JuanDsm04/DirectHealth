package com.uvg.directhealth.db

import java.time.LocalDateTime

class AppointmentDb {

    private val appointments: List<Appointment> = listOf(
        Appointment(
            id = "1",
            doctorId = "1",
            patientId = "2",
            date = LocalDateTime.of(2024, 3, 10, 10, 0)
        ),
        Appointment(
            id = "2",
            doctorId = "3",
            patientId = "2",
            date = LocalDateTime.of(2024, 4, 16, 10, 0)
        ),
        Appointment(
            id = "3",
            doctorId = "1",
            patientId = "7",
            date = LocalDateTime.of(2024, 4, 2, 9, 0)
        )
    )

    fun getAllAppointments(): List<Appointment> {
        return appointments
    }

    fun getAppointmentById(id: String): Appointment {
        return appointments.first { it.id == id }
    }

    fun getAppointmentsByDoctorId(doctorId: String): List<Appointment> {
        return appointments.filter { it.doctorId == doctorId }
    }

    fun getAppointmentsByPatientId(patientId: String): List<Appointment> {
        return appointments.filter { it.patientId == patientId }
    }
}