package com.uvg.directhealth.data.network.dto

import com.uvg.directhealth.domain.model.DoctorInfo
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.Specialty
import com.uvg.directhealth.domain.model.User
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class UserDto(
    val id: String,
    val role: String,
    val name: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val dpi: String,
    val phoneNumber: String,
    val medicalHistory: String?,
    val doctorInfo: DoctorInfoDto?
)

@Serializable
data class DoctorInfoDto(
    val number: Int,
    val address: String,
    val summary: String,
    val specialty: String
)

fun UserDto.toUser(): User {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val parsedDate = LocalDate.parse(this.birthDate, formatter)

    return User(
        id = this.id,
        role = Role.valueOf(this.role),
        name = this.name,
        password = this.password,
        email = this.email,
        birthDate = parsedDate,
        dpi = this.dpi,
        phoneNumber = this.phoneNumber,
        medicalHistory = this.medicalHistory,
        doctorInfo = this.doctorInfo?.toDoctorInfo()
    )
}

fun DoctorInfoDto.toDoctorInfo(): DoctorInfo {
    return DoctorInfo(
        number = this.number,
        address = this.address,
        summary = this.summary,
        specialty = Specialty.valueOf(this.specialty)
    )
}