package com.uvg.directhealth.layouts.mainFlow.user.directory

import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.User

data class UserDirectoryState (
    val userName: String? = null,
    val userRole: Role? = null,
    val userList: List<User> = emptyList(),
    val isLoading: Boolean = true
)