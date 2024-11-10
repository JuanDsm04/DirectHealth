package com.uvg.directhealth.layouts.mainFlow.prescription.list

import com.uvg.directhealth.data.model.Prescription
import com.uvg.directhealth.data.model.Role

data class PrescriptionListState (
    val prescriptionList: List<Prescription> = emptyList(),
    val role: Role? = null
)