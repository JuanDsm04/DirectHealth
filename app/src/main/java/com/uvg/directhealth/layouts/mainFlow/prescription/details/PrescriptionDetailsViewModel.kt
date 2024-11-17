package com.uvg.directhealth.layouts.mainFlow.prescription.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.toPrescription
import com.uvg.directhealth.data.network.repository.PrescriptionRepositoryImpl
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.repository.PrescriptionRepository
import com.uvg.directhealth.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.uvg.directhealth.util.Result
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PrescriptionDetailsViewModel(
    private val prescriptionRepository: PrescriptionRepository,
    private val userRepository: UserRepository

): ViewModel() {
    private val _state = MutableStateFlow(PrescriptionDetailsState())
    val state = _state.asStateFlow()

    fun onEvent(event: PrescriptionDetailsEvent) {
        when (event) {
            is PrescriptionDetailsEvent.PopulateData -> populateData(event.prescriptionId)
        }
    }

    private fun populateData(prescriptionId: String) {
        viewModelScope.launch {
            when (val result = prescriptionRepository.getPrescriptionById(prescriptionId)) {
                is Result.Success -> {
                    val prescription = result.data.toPrescription()
                    val userResult = userRepository.getUserById(prescription.patientId)
                    val userName = if(userResult is Result.Success) userResult.data.name else ""
                    val userBirthDate = if(userResult is Result.Success) userResult.data.birthDate else null

                    val patientAge = userBirthDate?.let {
                        try {
                            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                            val birthDate = LocalDate.parse(it, dateFormatter)
                            LocalDate.now().year - birthDate.year
                        } catch (e: Exception) {
                            0
                        }
                    } ?: 0

                    _state.update { state ->
                        state.copy(
                            prescription = prescription,
                            patientName = userName,
                            patientAge = patientAge,
                            isLoading = false,
                            hasError = false
                        )
                    }
                }

                is Result.Error -> {
                    _state.update { state ->
                        state.copy(
                            prescription = null,
                            isLoading = false,
                            hasError = true,
                        )
                    }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val prescriptionRepository = PrescriptionRepositoryImpl(api)
                val userRepositoryImpl = UserRepositoryImpl(api)

                PrescriptionDetailsViewModel(
                    prescriptionRepository = prescriptionRepository,
                    userRepository = userRepositoryImpl
                )
            }
        }
    }
}