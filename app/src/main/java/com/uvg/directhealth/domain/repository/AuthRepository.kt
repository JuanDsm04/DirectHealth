package com.uvg.directhealth.domain.repository

import com.uvg.directhealth.data.network.dto.LoginResponseDto
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponseDto, NetworkError>
}