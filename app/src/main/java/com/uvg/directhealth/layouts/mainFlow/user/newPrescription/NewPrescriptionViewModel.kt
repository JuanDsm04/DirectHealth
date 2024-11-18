package com.uvg.directhealth.layouts.mainFlow.user.newPrescription

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.model.Medication
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.PrescriptionDto
import com.uvg.directhealth.data.network.dto.mapToDto
import com.uvg.directhealth.data.network.dto.toUser
import com.uvg.directhealth.data.network.repository.AppointmentRepositoryImpl
import com.uvg.directhealth.data.network.repository.PrescriptionRepositoryImpl
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.data.source.UserDb
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.model.User
import com.uvg.directhealth.domain.repository.PrescriptionRepository
import com.uvg.directhealth.domain.repository.UserRepository
import com.uvg.directhealth.layouts.mainFlow.user.profile.UserProfileViewModel
import com.uvg.directhealth.util.Result
import com.uvg.directhealth.util.map
import com.uvg.directhealth.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class NewPrescriptionViewModel(
    private val userRepository: UserRepository,
    private val prescriptionRepository: PrescriptionRepository
): ViewModel() {
    private val _state = MutableStateFlow(NewPrescriptionState())
    val state = _state.asStateFlow()

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
            NewPrescriptionEvent.NewPrescription -> newPrescription()
        }
    }

    private fun populateData(loggedUserId: String, patientUserId: String) {
        viewModelScope.launch {
            val loggedUserResult = userRepository.getUserById(loggedUserId)
            val profileUserResult = userRepository.getUserById(patientUserId)

            if (loggedUserResult is Result.Success && profileUserResult is Result.Success) {
                val loggedUser = loggedUserResult.data.toUser()
                val userProfile = profileUserResult.data.toUser()

                _state.update { state ->
                    state.copy(
                        loggedUser = loggedUser,
                        patientUser = userProfile,
                    )
                }

            }
        }
    }

    private fun newPrescription() {
        val doctor: User = _state.value.loggedUser!!
        val patient: User = _state.value.patientUser!!

        val date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        viewModelScope.launch {
            prescriptionRepository
                .addPrescription(
                    PrescriptionDto(
                        doctorId = doctor.id,
                        patientId = patient.id,
                        emissionDate = df.format(date),
                        medicationList = _state.value.medicationList.map { it.mapToDto() },
                        notes = _state.value.noteList
                    )
                )
                .onSuccess {
                    _state.update { state ->
                        state.copy(
                            medicationList = emptyList(),
                            noteList = emptyList(),
                            isMedicationFormVisible = false,
                            isNoteFormVisible = false,
                            nameMedicine = "",
                            descriptionMedicine = "",
                            note = "",
                            isErrorCreatePrescription = false
                        )
                    }
                }
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val userRepository = UserRepositoryImpl(api)

                NewPrescriptionViewModel(
                    userRepository = userRepository,
                    prescriptionRepository = PrescriptionRepositoryImpl(api)
                )
            }
        }
    }
}