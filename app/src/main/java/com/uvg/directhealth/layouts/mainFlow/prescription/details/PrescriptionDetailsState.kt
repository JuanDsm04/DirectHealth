package com.uvg.directhealth.layouts.mainFlow.prescription.details

import com.uvg.directhealth.data.model.Prescription

data class PrescriptionDetailsState(
    val prescription: Prescription? = null,
    val patientName: String = "",
    val patientAge: Int = 0,
    val isLoading: Boolean = true,
    val hasError: Boolean = false
)