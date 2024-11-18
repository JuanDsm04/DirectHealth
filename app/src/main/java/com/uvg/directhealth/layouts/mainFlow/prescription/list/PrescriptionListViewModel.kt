package com.uvg.directhealth.layouts.mainFlow.prescription.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.data.model.Prescription
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.toPrescription
import com.uvg.directhealth.data.network.dto.toUser
import com.uvg.directhealth.data.network.repository.PrescriptionRepositoryImpl
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.dataStore
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.repository.PrescriptionRepository
import com.uvg.directhealth.domain.repository.UserRepository
import com.uvg.directhealth.util.Result
import com.uvg.directhealth.util.map
import com.uvg.directhealth.util.onError
import com.uvg.directhealth.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrescriptionListViewModel(
    private val userPrefs: DataStoreUserPrefs,
    private val prescriptionRepository: PrescriptionRepository,
    private val userRepository: UserRepository

) : ViewModel() {
    private val _state = MutableStateFlow(PrescriptionListState())
    val state = _state.asStateFlow()

    fun onEvent(event: PrescriptionListEvent) {
        when (event) {
            PrescriptionListEvent.PopulateData -> getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(
                    isLoading = true,
                    hasError = false
                )
            }

            val userId = userPrefs.getValue("userId")
            val roleString = userPrefs.getValue("role")
            val role = roleString?.let { Role.valueOf(it) }

            if (userId != null && role != null) {
                prescriptionRepository
                    .getAllPrescriptions(userId)
                    .map { data -> data.map { it.toPrescription() } }
                    .onSuccess { prescriptions ->
                        val usersResult = getUsersDetails(prescriptions)

                        _state.update { state ->
                            state.copy(
                                prescriptionList = prescriptions,
                                userDetails = usersResult,
                                role = role,
                                isLoading = false,
                                hasError = false
                            )
                        }
                    }
                    .onError {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                hasError = true
                            )
                        }
                    }

                } else {
                _state.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    private suspend fun getUsersDetails(prescriptions: List<Prescription>): Map<String, String> {
        val userIds = prescriptions.flatMap { listOf(it.doctorId, it.patientId) }.distinct()
        val users = mutableMapOf<String, String>()

        for (userId in userIds) {
            val result = userRepository.getUserById(userId)
            if (result is Result.Success) {
                val user = result.data.toUser()
                users[userId] = user.name
            }
        }
        return users
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val prescriptionRepository = PrescriptionRepositoryImpl(api)
                val userRepository = UserRepositoryImpl(api)

                PrescriptionListViewModel(
                    userPrefs = DataStoreUserPrefs(application.dataStore),
                    prescriptionRepository = prescriptionRepository,
                    userRepository = userRepository
                )
            }
        }
    }
}