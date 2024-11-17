package com.uvg.directhealth.domain.repository

import com.uvg.directhealth.data.network.dto.AppointmentDto
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

interface AppointmentRepository {
    suspend fun getAllAppointments(userId: String?): Result<List<AppointmentDto>, NetworkError>
    suspend fun getAppointmentById(id: String): Result<AppointmentDto, NetworkError>
    suspend fun addAppointment(appointment: AppointmentDto): Result<AppointmentDto, NetworkError>
}