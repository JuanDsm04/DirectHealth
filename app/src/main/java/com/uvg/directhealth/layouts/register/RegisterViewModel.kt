package com.uvg.directhealth.layouts.register

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class RegisterViewModel: ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.AddressChange -> onAddressChange(event.address)
            is RegisterEvent.BirthDateChange -> onBirthDateChange(event.birthDate)
            is RegisterEvent.DpiChange -> onDpiChange(event.dpi)
            is RegisterEvent.EmailChange -> onEmailChange(event.email)
            is RegisterEvent.ExperienceChange -> onExperienceChange(event.experience)
            is RegisterEvent.MedicalHistoryChange -> onMedicalHistoryChange(event.medicalHistory)
            is RegisterEvent.MembershipChange -> onMembershipChange(event.membership)
            is RegisterEvent.NameChange -> onNameChange(event.name)
            is RegisterEvent.PasswordChange -> onPasswordChange(event.password)
            is RegisterEvent.PhoneNumberChange -> onPhoneNumberChange(event.phoneNumber)
            is RegisterEvent.PasswordVisibleChange -> onPasswordVisibleChange()
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

    private fun onSpecialtyChange(name: String) {
        _state.update { state ->
            state.copy(
                name = name
            )
        }
    }
}