package com.uvg.directhealth.db

import com.uvg.directhealth.DoctorInfo
import com.uvg.directhealth.PatientInfo
import com.uvg.directhealth.Role
import com.uvg.directhealth.Specialty
import com.uvg.directhealth.User
import java.time.LocalDate

class UserDb {
    private val users: List<User> = listOf(
        User(
            id = "1",
            role = Role.DOCTOR,
            name = "Dr. Juan Pérez",
            email = "juan.perez@directhealth.com",
            password = "password123",
            birthDate = LocalDate.of(1975, 5, 12),
            dpi = "1234567890123",
            phoneNumber = "12345678",
            patientInfo = null,
            doctorInfo = DoctorInfo(
                number = 1122,
                address = "Calle Salud 123",
                summary = "Cardiólogo experimentado con más de 20 años en el campo.",
                specialty = Specialty.CARDIOLOGY
            )
        ),
        User(
            id = "2",
            role = Role.PATIENT,
            name = "Ana Martínez",
            email = "ana.martinez@gmail.com",
            password = "password123",
            birthDate = LocalDate.of(1990, 2, 20),
            dpi = "9876543210123",
            phoneNumber = "87654321",
            patientInfo = PatientInfo(
                medicalHistory = "Sin alergias conocidas. Cirugías previas: apendicectomía en 2010."
            ),
            doctorInfo = null
        ),
        User(
            id = "3",
            role = Role.DOCTOR,
            name = "Dra. Emma González",
            email = "emma.gonzalez@directhealth.com",
            password = "password123",
            birthDate = LocalDate.of(1980, 11, 7),
            dpi = "6543210987654",
            phoneNumber = "12398765",
            patientInfo = null,
            doctorInfo = DoctorInfo(
                number = 3344,
                address = "Carrera Médica 456",
                summary = "Dermatóloga especializada en tratamientos estéticos y trastornos de la piel.",
                specialty = Specialty.DERMATOLOGY
            )
        ),
        User(
            id = "4",
            role = Role.DOCTOR,
            name = "Dr. Carlos López",
            email = "carlos.lopez@directhealth.com",
            password = "password123",
            birthDate = LocalDate.of(1978, 8, 15),
            dpi = "3216549870123",
            phoneNumber = "23456789",
            patientInfo = null,
            doctorInfo = DoctorInfo(
                number = 4455,
                address = "Avenida Salud 789",
                summary = "Médico general con amplia experiencia en atención primaria.",
                specialty = Specialty.GENERAL
            )
        ),
        User(
            id = "5",
            role = Role.PATIENT,
            name = "Luis Fernández",
            email = "luis.fernandez@gmail.com",
            password = "password123",
            birthDate = LocalDate.of(1985, 4, 10),
            dpi = "1472583690123",
            phoneNumber = "34567890",
            patientInfo = PatientInfo(
                medicalHistory = "Alergia a la penicilina. Sin cirugías previas."
            ),
            doctorInfo = null
        ),
        User(
            id = "6",
            role = Role.DOCTOR,
            name = "Dra. Sofia Torres",
            email = "sofia.torres@directhealth.com",
            password = "password123",
            birthDate = LocalDate.of(1992, 1, 22),
            dpi = "9638527410123",
            phoneNumber = "45678901",
            patientInfo = null,
            doctorInfo = DoctorInfo(
                number = 5566,
                address = "Boulevard Médico 321",
                summary = "Especialista en pediatría con enfoque en el bienestar infantil.",
                specialty = Specialty.PEDIATRICS
            )
        ),
        User(
            id = "7",
            role = Role.PATIENT,
            name = "María González",
            email = "maria.gonzalez@gmail.com",
            password = "password123",
            birthDate = LocalDate.of(1995, 7, 30),
            dpi = "8523697410123",
            phoneNumber = "56789012",
            patientInfo = PatientInfo(
                medicalHistory = "Sin alergias conocidas. Cirugía de rodilla en 2018."
            ),
            doctorInfo = null
        )
    )

    fun getAllUsers(): List<User> {
        return users
    }

    fun getUserById(id: String): User {
        return users.first { it.id == id }
    }

    fun getAllDoctors(): List<User> {
        return users.filter { it.role == Role.DOCTOR }
    }

    fun getAllPatients(): List<User> {
        return users.filter { it.role == Role.PATIENT }
    }

    fun getPatientsByDoctorId(doctorId: String, appointmentDb: AppointmentDb): List<User> {
        val appointments = appointmentDb.getAppointmentsByDoctorId(doctorId)
        val patientIds = appointments.map { it.patientId }.distinct()
        return getAllPatients().filter { it.id in patientIds }
    }

}