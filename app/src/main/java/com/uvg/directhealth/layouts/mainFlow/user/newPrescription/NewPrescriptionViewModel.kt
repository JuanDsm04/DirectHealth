package com.uvg.directhealth.layouts.mainFlow.user.newPrescription

import androidx.lifecycle.ViewModel
import com.uvg.directhealth.data.model.Medication
import com.uvg.directhealth.data.source.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewPrescriptionViewModel: ViewModel() {
    private val _state = MutableStateFlow(NewPrescriptionState())
    val state = _state.asStateFlow()

    private val userDb = UserDb()

    private val medicationList = mutableListOf<Medication>()
    private val noteList = mutableListOf<String>()

    fun onEvent(event: NewPrescriptionEvent) {
        when (event) {
            is NewPrescriptionEvent.PopulateData -> populateData(event.loggedUserId, event.patientUserId)
            is NewPrescriptionEvent.ToggleMedicationFormVisibility -> {
                _state.value = _state.value.copy(isMedicationFormVisible = !_state.value.isMedicationFormVisible)
            }
            is NewPrescriptionEvent.ToggleNoteFormVisibility -> {
                _state.value = _state.value.copy(isNoteFormVisible = !_state.value.isNoteFormVisible)
            }

            is NewPrescriptionEvent.NameMedicineChange -> onNameMedicineChange(event.name)
            is NewPrescriptionEvent.DescriptionMedicineChange -> onDescriptionMedicineChange(event.description)
            is NewPrescriptionEvent.NoteChange -> onNoteChange(event.note)
        }
    }

    private fun populateData(loggedUserId: String, patientUserId: String) {
        val loggedUser = userDb.getUserById(loggedUserId)
        val patientUser = userDb.getUserById(patientUserId)

        _state.update { state ->
            state.copy(
                loggedUser = loggedUser,
                patientUser = patientUser
            )
        }
    }

    private fun onNameMedicineChange(nameMedicine: String) {
        _state.update { state ->
            state.copy(
                nameMedicine = nameMedicine
            )
        }
    }

    private fun onDescriptionMedicineChange(descriptionMedicine: String) {
        _state.update { state ->
            state.copy(
                descriptionMedicine = descriptionMedicine
            )
        }
    }

    private fun onNoteChange(note: String) {
        _state.update { state ->
            state.copy(
                note = note
            )
        }
    }

    private fun addMedication(name: String, description: String) {
        val newMedication = Medication(name, description)
        medicationList.add(newMedication)

        _state.update { state ->
            state.copy(
                medicationList = medicationList.toList()
            )
        }
    }

    private fun addNote(note: String) {
        noteList.add(note)

        _state.update { state ->
            state.copy(
                noteList = noteList.toList()
            )
        }
    }
}