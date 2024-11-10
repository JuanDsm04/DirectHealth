package com.uvg.directhealth.layouts.mainFlow.prescription.details

import androidx.lifecycle.ViewModel
import com.uvg.directhealth.data.source.PrescriptionDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PrescriptionDetailsViewModel: ViewModel() {
    private val _state = MutableStateFlow(PrescriptionDetailsState())
    val state = _state.asStateFlow()

    private val prescriptionDb = PrescriptionDb()

    fun onEvent(event: PrescriptionDetailsEvent) {
        when (event) {
            is PrescriptionDetailsEvent.PopulateData -> populateData(event.prescriptionId)
        }
    }

    private fun populateData(prescriptionId: String) {
        val prescription = prescriptionDb.getPrescriptionById(prescriptionId)

        _state.update { state ->
            state.copy(
                prescription = prescription
            )
        }
    }
}