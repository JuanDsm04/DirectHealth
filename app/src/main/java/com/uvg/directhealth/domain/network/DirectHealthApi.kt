package com.uvg.directhealth.domain.network

import com.uvg.directhealth.data.network.dto.AppointmentDto
import com.uvg.directhealth.data.network.dto.LoginResponseDto
import com.uvg.directhealth.data.network.dto.PrescriptionDto
import com.uvg.directhealth.data.network.dto.RegisterDto
import com.uvg.directhealth.data.network.dto.UserDto
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

interface DirectHealthApi {
    suspend fun login(email: String, password: String): Result<LoginResponseDto, NetworkError>
    suspend fun register(registerDto: RegisterDto): Result<String, NetworkError>

    suspend fun getAllUsers(): Result<List<UserDto>, NetworkError>
    suspend fun getUserById(id: String): Result<UserDto, NetworkError>
    suspend fun updateUser(id: String, user: UserDto): Result<UserDto, NetworkError>

    suspend fun getAllPrescriptions(userId: String?): Result<List<PrescriptionDto>, NetworkError>
    suspend fun getPrescriptionById(id: String): Result<PrescriptionDto, NetworkError>
    suspend fun addPrescription(prescription: PrescriptionDto): Result<PrescriptionDto, NetworkError>

    suspend fun getAllAppointments(userId: String?): Result<List<AppointmentDto>, NetworkError>
    suspend fun getAppointmentById(id: String): Result<AppointmentDto, NetworkError>
    suspend fun addAppointment(appointment: AppointmentDto): Result<AppointmentDto, NetworkError>
}