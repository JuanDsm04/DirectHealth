package com.uvg.directhealth.layouts.register

import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.Specialty

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    var birthDate: String = "",
    val dpi: String = "",
    val phoneNumber: String = "",
    val medicalHistory: String = "",
    val membership: String = "",
    val address: String = "",
    val experience: String = "",
    val specialty: Specialty? = null,
    val isPasswordVisible: Boolean = false,
    val hasPasswordError: Boolean = false,
    val hasDpiError: Boolean = false,
    val hasPhoneNumberError: Boolean = false,
    val hasMembershipError: Boolean = false,
    val hasBirthDateError: Boolean = false,
    val hasEmailError: Boolean = false,
    val role: Role,
    val successfulRegistration: Boolean = false
)