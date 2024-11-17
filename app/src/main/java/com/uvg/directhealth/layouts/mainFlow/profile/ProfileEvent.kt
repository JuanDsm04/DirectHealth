package com.uvg.directhealth.layouts.mainFlow.profile

sealed interface ProfileEvent {
    data class PopulateData(val loggedUserId: String): ProfileEvent
    data class NameChange(val name: String): ProfileEvent
    data class EmailChange(val email: String): ProfileEvent
    data class PasswordChange(val password: String): ProfileEvent
    data class BirthDateChange(val birthDate: String): ProfileEvent
    data class DpiChange(val dpi: String): ProfileEvent
    data class PhoneNumberChange(val phoneNumber: String): ProfileEvent
    data class MedicalHistoryChange(val medicalHistory: String): ProfileEvent
    data class MembershipChange(val membership: String): ProfileEvent
    data class AddressChange(val address: String): ProfileEvent
    data class ExperienceChange(val experience: String): ProfileEvent
    data object PasswordVisibleChange: ProfileEvent
    data object SaveProfile : ProfileEvent
}