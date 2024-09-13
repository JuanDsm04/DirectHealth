package com.uvg.directhealth.db

import Medication
import Prescription
import java.time.LocalDate

class PrescriptionDb(private val userDb: UserDb) {
    private val prescriptions: List<Prescription> = listOf(
        Prescription(
            id = "1",
            doctorId = "1",
            patientId = "2",
            emissionDate = LocalDate.of(2024, 1, 12),
            medicationList = listOf(
                Medication(
                    name = "Aspirina",
                    description = "Para aliviar el dolor y reducir la inflamación."
                ),
                Medication(
                    name = "Atorvastatina",
                    description = "Ayuda a reducir los niveles de colesterol."
                )
            ),
            notes = listOf(
                "Tome aspirina una vez al día después de las comidas.",
                "Controlar los niveles de colesterol cada 3 meses."
            )
        ),
        Prescription(
            id = "2",
            doctorId = "3",
            patientId = "2",
            emissionDate = LocalDate.of(2024, 8, 27),
            medicationList = listOf(
                Medication(
                    name = "Hidrocortisona",
                    description = "Se utiliza para reducir la inflamación y tratar afecciones de la piel."
                )
            ),
            notes = listOf(
                "Aplicar hidrocortisona dos veces al día en las zonas de piel afectadas."
            )
        )
    )

    fun getAllPrescriptions(): List<Prescription> {
        return prescriptions
    }

    fun getPrescriptionById(id: String): Prescription {
        return prescriptions.first { it.id == id }
    }

    fun getPrescriptionsByDoctorId(doctorId: String): List<Prescription> {
        return prescriptions.filter { it.doctorId == doctorId }
    }

    fun getPrescriptionsByPatientId(patientId: String): List<Prescription> {
        return prescriptions.filter { it.patientId == patientId }
    }
}