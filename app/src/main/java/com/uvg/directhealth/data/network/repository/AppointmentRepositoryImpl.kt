package com.uvg.directhealth.data.network.repository

import com.uvg.directhealth.data.network.dto.AppointmentDto
import com.uvg.directhealth.domain.network.DirectHealthApi
import com.uvg.directhealth.domain.repository.AppointmentRepository
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

class AppointmentRepositoryImpl(private val directHealthApi: DirectHealthApi) : AppointmentRepository {
    override suspend fun getAllAppointments(userId: String?): Result<List<AppointmentDto>, NetworkError> {
        return when (val result = directHealthApi.getAllAppointments(userId)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getAppointmentById(id: String): Result<AppointmentDto, NetworkError> {
        return when (val result = directHealthApi.getAppointmentById(id)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun addAppointment(appointment: AppointmentDto): Result<AppointmentDto, NetworkError> {
        return when (val result = directHealthApi.addAppointment(appointment)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error)
        }
    }
}