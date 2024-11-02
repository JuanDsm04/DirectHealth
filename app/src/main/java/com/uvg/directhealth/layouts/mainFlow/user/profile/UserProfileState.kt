package com.uvg.directhealth.layouts.mainFlow.user.profile

import com.uvg.directhealth.data.model.User

data class UserProfileState(
    val loggedUser: User? = null,
    val userProfile: User? = null
)