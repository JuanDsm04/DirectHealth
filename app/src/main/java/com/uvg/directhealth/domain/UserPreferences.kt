package com.uvg.directhealth.domain

import com.uvg.directhealth.domain.model.Role

interface UserPreferences {
    suspend fun setRole(role: Role)
    suspend fun setUserId(id: String)
    suspend fun getValue(key: String?): String?
}