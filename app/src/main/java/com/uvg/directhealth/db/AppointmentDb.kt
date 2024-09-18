package com.uvg.directhealth.db

import java.time.LocalDateTime

class AppointmentDb {

    private val appointments: MutableList<Appointment> = mutableListOf(
        Appointment(
            id = "1",
            doctorId = "1",
            patientId = "2",
            date = LocalDateTime.of(2024, 9, 29, 10, 0)
        ),
        Appointment(
            id = "2",
            doctorId = "3",
            patientId = "2",
            date = LocalDateTime.of(2024, 9, 20, 10, 0)
        ),
        Appointment(
            id = "3",
            doctorId = "1",
            patientId = "7",
            date = LocalDateTime.of(2024, 10, 2, 9, 0)
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

    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
    }
}