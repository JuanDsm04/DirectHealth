package com.uvg.directhealth.domain

import com.uvg.directhealth.domain.model.Role
import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    fun authStatus(): Flow<Boolean>
    suspend fun setRole(role: Role)
    suspend fun setUserId(id: String)
    suspend fun getValue(key: String?): String?
    suspend fun logIn()
    suspend fun logOut()
}