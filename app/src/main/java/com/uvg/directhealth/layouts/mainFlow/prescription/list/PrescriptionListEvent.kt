package com.uvg.directhealth.layouts.mainFlow.prescription.list

sealed interface PrescriptionListEvent {
    data object PopulateData: PrescriptionListEvent
}