package com.uvg.directhealth.layouts.mainFlow.prescription.list

import com.uvg.directhealth.data.model.Prescription
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.User
import java.lang.Error

data class PrescriptionListState (
    val prescriptionList: List<Prescription> = emptyList(),
    val userDetails: Map<String, String> = emptyMap(),
    val role: Role? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false
)