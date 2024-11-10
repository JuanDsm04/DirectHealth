package com.uvg.directhealth.layouts.mainFlow.prescription.details

sealed interface PrescriptionDetailsEvent {
    data class PopulateData(val prescriptionId: String): PrescriptionDetailsEvent
}