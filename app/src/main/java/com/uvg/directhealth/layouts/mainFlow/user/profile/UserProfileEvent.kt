package com.uvg.directhealth.layouts.mainFlow.user.profile

sealed interface UserProfileEvent {
    data class PopulateData(
        val loggedUserId: String,
        val userProfileId: String
    ): UserProfileEvent

    data class ScheduleAppointment(val userID: String): UserProfileEvent
    data class UpdateSelectedDate(val date: String): UserProfileEvent
    data class UpdateSelectedTime(val time: String): UserProfileEvent
    data class ToggleDatePicker(val show: Boolean) : UserProfileEvent
    data class ToggleTimePicker(val show: Boolean) : UserProfileEvent
    data object ResetSuccessfulCreateAppointment : UserProfileEvent
}