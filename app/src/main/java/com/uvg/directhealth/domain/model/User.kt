package com.uvg.directhealth.domain.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class Role {
    PATIENT,
    DOCTOR
}

enum class Specialty {
    GENERAL,
    CARDIOLOGY,
    DERMATOLOGY,
    ENDOCRINOLOGY,
    GASTROENTEROLOGY,
    GYNECOLOGY,
    HEMATOLOGY,
    INFECTIOLOGY,
    NEPHROLOGY,
    NEUROLOGY,
    PULMONOLOGY,
    OPHTHALMOLOGY,
    ONCOLOGY,
    ORTHOPEDICS,
    OTOLARYNGOLOGY,
    PEDIATRICS,
    PSYCHIATRY,
    RADIOLOGY,
    RHEUMATOLOGY,
    TRAUMATOLOGY,
    UROLOGY,
    ALLERGOLOGY,
    ANGIOLOGY,
    PLASTIC,
    GERIATRICS
}

data class User(
    val id: String,
    val role: Role,
    val name: String,
    val email: String,
    val password: String,
    val birthDate: LocalDate,
    val dpi: String,
    val phoneNumber: String,
    val medicalHistory: String?,
    val doctorInfo: DoctorInfo?
)

@Serializable
data class DoctorInfo(
    val number: Int,
    val address: String,
    val summary: String,
    val specialty: Specialty
)