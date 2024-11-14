package com.uvg.directhealth.data.network.repository

import com.uvg.directhealth.data.network.dto.LoginResponseDto
import com.uvg.directhealth.data.network.dto.RegisterDto
import com.uvg.directhealth.domain.network.DirectHealthApi
import com.uvg.directhealth.domain.repository.AuthRepository
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

class AuthRepositoryImpl(private val directHealthApi: DirectHealthApi): AuthRepository {
    override suspend fun login(email: String, password: String): Result<LoginResponseDto, NetworkError> {
        when (val result = directHealthApi.login(email, password)) {
            is Result.Error -> {
                println(result)

                return Result.Error(NetworkError.UNKNOWN)
            }

            is Result.Success -> {
                return Result.Success(result.data)
            }
        }
    }

    override suspend fun register(registerDto: RegisterDto): Result<String, NetworkError> {
        when (val result = directHealthApi.register(registerDto)) {
            is Result.Error -> {
                println(result)

                return Result.Error(NetworkError.UNKNOWN)
            }

            is Result.Success -> {
                return Result.Success(result.data)
            }
        }
    }
}