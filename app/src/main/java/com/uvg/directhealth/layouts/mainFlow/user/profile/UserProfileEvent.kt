package com.uvg.directhealth.layouts.mainFlow.user.profile

sealed interface UserProfileEvent {
    data class PopulateData(val loggedUserId: String, val userProfileId: String): UserProfileEvent
}