package com.uvg.directhealth.layouts.mainFlow.profile

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.uvg.directhealth.data.model.Specialty
import com.uvg.directhealth.data.source.UserDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProfileViewModel: ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val userDb = UserDb()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.PopulateData -> populateData(event.loggedUserId)
            is ProfileEvent.AddressChange -> onAddressChange(event.address)
            is ProfileEvent.BirthDateChange -> onBirthDateChange(event.birthDate)
            is ProfileEvent.DpiChange -> onDpiChange(event.dpi)
            is ProfileEvent.EmailChange -> onEmailChange(event.email)
            is ProfileEvent.ExperienceChange -> onExperienceChange(event.experience)
            is ProfileEvent.MedicalHistoryChange -> onMedicalHistoryChange(event.medicalHistory)
            is ProfileEvent.MembershipChange -> onMembershipChange(event.membership)
            is ProfileEvent.NameChange -> onNameChange(event.name)
            is ProfileEvent.PasswordChange -> onPasswordChange(event.password)
            is ProfileEvent.PhoneNumberChange -> onPhoneNumberChange(event.phoneNumber)
            is ProfileEvent.PasswordVisibleChange -> onPasswordVisibleChange()
        }
    }

    private fun formatDate(birthDateString: String): String {
        val formatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatOutput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date = formatInput.parse(birthDateString) ?: Date()
        
        return formatOutput.format(date)
    }

    private fun populateData(loggedUserId: String) {
        val loggedUser = userDb.getUserById(loggedUserId)

        _state.update { state ->
            state.copy(
                loggedUser = loggedUser,
                name = loggedUser.name,
                email = loggedUser.email,
                password = loggedUser.password,
                birthDate = formatDate(loggedUser.birthDate.toString()),
                dpi = loggedUser.dpi,
                phoneNumber = loggedUser.phoneNumber,
                medicalHistory = loggedUser.patientInfo?.medicalHistory ?: "",
                membership = loggedUser.doctorInfo?.number?.toString() ?: "",
                address = loggedUser.doctorInfo?.address ?: "",
                experience = loggedUser.doctorInfo?.summary ?: "",
                specialty = loggedUser.doctorInfo?.specialty
            )
        }
    }

    private fun onNameChange(name: String) {
        _state.update { state ->
            state.copy(
                name = name
            )
        }
    }

    private fun onEmailChange(email: String) {
        val emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        val hasError = !email.matches(emailPattern.toRegex())

        _state.update { state ->
            state.copy(
                email = email,
                hasEmailError = hasError
            )
        }
    }

    private fun onPasswordChange(password: String) {
        val hasError = password.length <= 8

        _state.update { state ->
            state.copy(
                password = password,
                hasPasswordError = hasError
            )
        }
    }

    private fun onPasswordVisibleChange() {
        _state.update { state ->
            state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    private fun onBirthDateChange(birthDate: String) {
        val parts = birthDate.split("/")
        val day = parts[0].toIntOrNull() ?: return
        val month = parts[1].toIntOrNull()?.minus(1) ?: return
        val year = parts[2].toIntOrNull() ?: return

        val birthDateCalendar = Calendar.getInstance().apply {
            set(year, month, day)
        }

        val currentDate = Calendar.getInstance()
        val age = currentDate.get(Calendar.YEAR) - birthDateCalendar.get(Calendar.YEAR)
        val hasError = age < 18 || (age == 18 && (currentDate.get(Calendar.MONTH) < month ||
                (currentDate.get(Calendar.MONTH) == month && currentDate.get(Calendar.DAY_OF_MONTH) < day)))

        _state.update { state ->
            state.copy(
                birthDate = birthDate,
                hasBirthDateError = hasError
            )
        }
    }

    private fun onDpiChange(dpi: String) {
        val hasError = !dpi.isDigitsOnly()

        _state.update { state ->
            state.copy(
                dpi = dpi,
                hasDpiError = hasError
            )
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) {
        val hasError = !phoneNumber.isDigitsOnly() || phoneNumber.length <= 8

        _state.update { state ->
            state.copy(
                phoneNumber = phoneNumber,
                hasPhoneNumberError = hasError
            )
        }
    }

    private fun onMedicalHistoryChange(medicalHistory: String) {
        _state.update { state ->
            state.copy(
                medicalHistory = medicalHistory
            )
        }
    }

    private fun onMembershipChange(membership: String) {
        val hasError = !membership.isDigitsOnly()

        _state.update { state ->
            state.copy(
                membership = membership,
                hasMembershipError = hasError
            )
        }
    }

    private fun onAddressChange(address: String) {
        _state.update { state ->
            state.copy(
                address = address
            )
        }
    }

    private fun onExperienceChange(experience: String) {
        _state.update { state ->
            state.copy(
                experience = experience
            )
        }
    }

    private fun onSpecialtyChange(specialty: Specialty) {
        _state.update { state ->
            state.copy(
                specialty = specialty
            )
        }
    }
}