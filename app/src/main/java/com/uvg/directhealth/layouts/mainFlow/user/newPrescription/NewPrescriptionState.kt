package com.uvg.directhealth.layouts.mainFlow.user.newPrescription

import com.uvg.directhealth.data.model.Medication
import com.uvg.directhealth.data.model.User

data class NewPrescriptionState (
    val loggedUser: User? = null,
    val patientUser: User? =null,
    val medicationList: List<Medication> = emptyList(),
    val noteList: List<String> = emptyList(),
    val isMedicationFormVisible: Boolean = false,
    val isNoteFormVisible: Boolean = false,
    val nameMedicine: String = "",
    val descriptionMedicine: String = "",
    val note: String = "",
    val isErrorCreatePrescription: Boolean = false
)