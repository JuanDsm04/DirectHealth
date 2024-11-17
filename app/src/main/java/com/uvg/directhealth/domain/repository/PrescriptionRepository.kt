package com.uvg.directhealth.domain.repository

import com.uvg.directhealth.data.network.dto.PrescriptionDto
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

interface PrescriptionRepository {
    suspend fun getAllPrescriptions(userId: String?): Result<List<PrescriptionDto>, NetworkError>
    suspend fun getPrescriptionById(id: String): Result<PrescriptionDto, NetworkError>
    suspend fun addPrescription(prescription: PrescriptionDto): Result<PrescriptionDto, NetworkError>
}
