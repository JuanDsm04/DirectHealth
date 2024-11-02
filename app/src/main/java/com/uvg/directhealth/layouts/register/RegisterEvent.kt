package com.uvg.directhealth.layouts.register

sealed interface RegisterEvent {
    data class NameChange(val name: String): RegisterEvent
    data class EmailChange(val email: String): RegisterEvent
    data class PasswordChange(val password: String): RegisterEvent
    data class BirthDateChange(val birthDate: String): RegisterEvent
    data class DpiChange(val dpi: String): RegisterEvent
    data class PhoneNumberChange(val phoneNumber: String): RegisterEvent
    data class MedicalHistoryChange(val medicalHistory: String): RegisterEvent
    data class MembershipChange(val membership: String): RegisterEvent
    data class AddressChange(val address: String): RegisterEvent
    data class ExperienceChange(val experience: String): RegisterEvent
    data object PasswordVisibleChange: RegisterEvent
}