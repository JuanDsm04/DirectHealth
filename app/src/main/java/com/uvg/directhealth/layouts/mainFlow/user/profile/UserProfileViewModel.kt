package com.uvg.directhealth.layouts.mainFlow.user.profile

import androidx.lifecycle.ViewModel
import com.uvg.directhealth.data.source.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserProfileViewModel: ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    private val userDb = UserDb()

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.PopulateData -> populateData(event.loggedUserId, event.userProfileId)
        }
    }

    private fun populateData(loggedUserId: String, profileUserId: String) {
        val loggedUser = userDb.getUserById(loggedUserId)
        val userProfile = userDb.getUserById(profileUserId)

        _state.update { state ->
            state.copy(
                loggedUser = loggedUser,
                userProfile = userProfile
            )
        }
    }
}