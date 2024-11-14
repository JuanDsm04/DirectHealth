package com.uvg.directhealth.data.network.dto

import com.uvg.directhealth.domain.model.DoctorInfo
import com.uvg.directhealth.domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    val email: String,
    val name: String,
    val password: String,
    val birthDate: String,
    val dpi: String,
    val phoneNumber: String,
    val medicalHistory: String?,
    val doctorInfo: DoctorInfo?,
    val role: Role
)