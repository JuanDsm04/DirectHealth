package com.uvg.directhealth.domain.network

import com.uvg.directhealth.data.network.dto.LoginResponseDto
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

interface DirectHealthApi {
    suspend fun login(email: String, password: String): Result<LoginResponseDto, NetworkError>
}