package com.uvg.directhealth.domain.repository

import com.uvg.directhealth.data.network.dto.UserDto
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

interface UserRepository {
    suspend fun getAllUsers(): Result<List<UserDto>, NetworkError>
    suspend fun getUserById(id: String): Result<UserDto, NetworkError>
    suspend fun updateUser(id: String, user: UserDto): Result<UserDto, NetworkError>
}