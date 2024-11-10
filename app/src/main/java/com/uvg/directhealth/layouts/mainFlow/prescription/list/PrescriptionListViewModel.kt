package com.uvg.directhealth.layouts.mainFlow.prescription.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.uvg.directhealth.data.model.Role
import com.uvg.directhealth.data.source.PrescriptionDb
import com.uvg.directhealth.data.source.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PrescriptionListViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    private val prescriptionList = savedStateHandle.toRoute<PrescriptionListDestination>()
    private val _state = MutableStateFlow(PrescriptionListState())
    val state = _state.asStateFlow()

    private val userDb = UserDb()
    private val prescriptionDb = PrescriptionDb()

    fun onEvent(event: PrescriptionListEvent) {
        when (event) {
            PrescriptionListEvent.PopulateData -> getData()
        }
    }

    private fun getData() {
        val user = userDb.getUserById(prescriptionList.userId)

        _state.update { state ->
            if (user.role == Role.DOCTOR) {
                state.copy(
                    prescriptionList = prescriptionDb.getPrescriptionsByDoctorId(user.id),
                    role = Role.DOCTOR
                )

            } else {
                state.copy(
                    prescriptionList = prescriptionDb.getPrescriptionsByPatientId(user.id),
                    role = Role.PATIENT
                )
            }
        }
    }
}