package com.uvg.directhealth.data.network.dto

import com.uvg.directhealth.data.model.Prescription
import com.uvg.directhealth.data.model.Medication
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class PrescriptionDto(
    val id: String? = null,
    val doctorId: String,
    val patientId: String,
    val emissionDate: String,
    val medicationList: List<MedicationDto>,
    val notes: List<String>? = null
)

@Serializable
data class MedicationDto(
    val name: String,
    val description: String?
)

fun PrescriptionDto.toPrescription(): Prescription {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val parsedDate = LocalDate.parse(this.emissionDate, formatter)

    val medications = this.medicationList.map { it.toMedication() }

    return Prescription(
        id = this.id ?: "",
        doctorId = this.doctorId,
        patientId = this.patientId,
        emissionDate = parsedDate,
        medicationList = medications,
        notes = this.notes?: emptyList()
    )
}

fun MedicationDto.toMedication(): Medication {
    return Medication(
        name = this.name,
        description = this.description ?: ""
    )
}

fun Medication.mapToDto(): MedicationDto {
    return MedicationDto(
        name = this.name,
        description = this.description
    )
}
