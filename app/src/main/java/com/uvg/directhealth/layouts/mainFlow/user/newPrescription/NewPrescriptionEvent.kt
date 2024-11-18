package com.uvg.directhealth.layouts.mainFlow.user.newPrescription

sealed interface NewPrescriptionEvent {
    data class PopulateData (
        val loggedUserId: String,
        val patientUserId: String
    ): NewPrescriptionEvent

    data class NameMedicineChange(val name: String) : NewPrescriptionEvent
    data class DescriptionMedicineChange(val description: String) : NewPrescriptionEvent
    data class NoteChange(val note: String) : NewPrescriptionEvent

    data object NewPrescription: NewPrescriptionEvent
    data object ToggleMedicationFormVisibility : NewPrescriptionEvent
    data object ToggleNoteFormVisibility : NewPrescriptionEvent

    data class AddMedication(val name: String, val description: String) : NewPrescriptionEvent
    data class AddNote(val note: String) : NewPrescriptionEvent
    data object ResetPrescription : NewPrescriptionEvent
}