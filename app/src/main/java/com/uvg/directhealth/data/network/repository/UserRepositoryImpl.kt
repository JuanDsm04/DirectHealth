package com.uvg.directhealth.data.network.repository

import com.uvg.directhealth.data.network.dto.UserDto
import com.uvg.directhealth.domain.network.DirectHealthApi
import com.uvg.directhealth.domain.repository.UserRepository
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

class UserRepositoryImpl(private val directHealthApi: DirectHealthApi): UserRepository {

    override suspend fun getAllUsers(): Result<List<UserDto>, NetworkError> {
        when (val result = directHealthApi.getAllUsers()) {
            is Result.Error -> {
                println(result)
                return Result.Error(NetworkError.UNKNOWN)
            }
            is Result.Success -> {
                return Result.Success(result.data)
            }
        }
    }

    override suspend fun getUserById(id: String): Result<UserDto, NetworkError> {
        when (val result = directHealthApi.getUserById(id)) {
            is Result.Error -> {
                println(result)
                return Result.Error(NetworkError.UNKNOWN)
            }
            is Result.Success -> {
                return Result.Success(result.data)
            }
        }
    }

    override suspend fun updateUser(id: String, user: UserDto): Result<UserDto, NetworkError> {
        when (val result = directHealthApi.updateUser(id, user)) {
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