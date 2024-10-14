package com.uvg.directhealth.data.model

import java.time.LocalDate

data class Prescription(
    val id: String,
    val doctorId: String,
    val patientId: String,
    val emissionDate: LocalDate,
    val medicationList: List<Medication>,
    val notes: List<String>
)

data class Medication(
    val name: String,
    val description: String
)