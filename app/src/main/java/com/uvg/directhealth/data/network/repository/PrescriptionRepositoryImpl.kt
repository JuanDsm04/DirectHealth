package com.uvg.directhealth.data.network.repository

import com.uvg.directhealth.data.network.dto.PrescriptionDto
import com.uvg.directhealth.domain.network.DirectHealthApi
import com.uvg.directhealth.domain.repository.PrescriptionRepository
import com.uvg.directhealth.util.NetworkError
import com.uvg.directhealth.util.Result

class PrescriptionRepositoryImpl(private val directHealthApi: DirectHealthApi) : PrescriptionRepository {

    override suspend fun getAllPrescriptions(userId: String?): Result<List<PrescriptionDto>, NetworkError> {
        return when (val result = directHealthApi.getAllPrescriptions(userId)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getPrescriptionById(id: String): Result<PrescriptionDto, NetworkError> {
        return when (val result = directHealthApi.getPrescriptionById(id)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun addPrescription(prescription: PrescriptionDto): Result<PrescriptionDto, NetworkError> {
        return when (val result = directHealthApi.addPrescription(prescription)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error)
        }
    }
}
