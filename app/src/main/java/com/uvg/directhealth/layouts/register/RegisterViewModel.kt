package com.uvg.directhealth.layouts.register

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.uvg.directhealth.data.local.DataStoreUserPrefs
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.RegisterDto
import com.uvg.directhealth.data.network.repository.AuthRepositoryImpl
import com.uvg.directhealth.dataStore
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.UserPreferences
import com.uvg.directhealth.domain.model.DoctorInfo
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.model.Specialty
import com.uvg.directhealth.domain.repository.AuthRepository
import com.uvg.directhealth.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterViewModel(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val register = savedStateHandle.toRoute<RegisterDestination>()
    private val _state = MutableStateFlow(
        RegisterState(role = register.roleUser)
    )
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
            is RegisterEvent.SpecialtyChange -> onSpecialtyChange(event.specialty)
            is RegisterEvent.Register -> onRegister()
        }
    }

    fun isFormValid(role: Role): Boolean {
        return when (role) {
            Role.PATIENT -> {
                state.value.name.isNotEmpty() &&
                state.value.email.isNotEmpty() &&
                state.value.password.isNotEmpty() &&
                state.value.birthDate.isNotEmpty() &&
                state.value.dpi.isNotEmpty() &&
                state.value.phoneNumber.isNotEmpty() &&
                state.value.medicalHistory.isNotEmpty()
            }
            Role.DOCTOR -> {
                state.value.name.isNotEmpty() &&
                state.value.email.isNotEmpty() &&
                state.value.password.isNotEmpty() &&
                state.value.birthDate.isNotEmpty() &&
                state.value.dpi.isNotEmpty() &&
                state.value.phoneNumber.isNotEmpty() &&
                state.value.membership.isNotEmpty() &&
                state.value.address.isNotEmpty() &&
                state.value.experience.isNotEmpty() &&
                state.value.specialty != null
            }
        }
    }

    private fun onRegister() {
        viewModelScope.launch {

            if (register.roleUser == Role.PATIENT) {
                authRepository
                    .register(
                        RegisterDto(
                            email = _state.value.email,
                            name = _state.value.name,
                            password = _state.value.password,
                            birthDate = _state.value.birthDate,
                            dpi = _state.value.dpi,
                            phoneNumber = _state.value.phoneNumber,
                            medicalHistory = _state.value.medicalHistory,
                            doctorInfo = null,
                            role = _state.value.role
                        )
                    )
                    .onSuccess {
                        _state.update { state ->
                            state.copy(
                                successfulRegistration = true
                            )
                        }
                    }

            } else {
                println(_state.value.specialty)
                val specialty: Specialty = _state.value.specialty!!

                authRepository
                    .register(
                        RegisterDto(
                            email = _state.value.email,
                            name = _state.value.name,
                            password = _state.value.password,
                            birthDate = _state.value.birthDate,
                            dpi = _state.value.dpi,
                            phoneNumber = _state.value.phoneNumber,
                            medicalHistory = null,
                            doctorInfo =
                            DoctorInfo(
                                number = _state.value.membership.toInt(),
                                address = _state.value.address,
                                summary = _state.value.experience,
                                specialty = specialty
                            ),
                            role = _state.value.role
                        )
                    ).onSuccess {
                        _state.update { state ->
                            state.copy(
                                successfulRegistration = true
                            )
                        }
                    }
            }
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
        val hasError = password.length < 8

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
        val parts = birthDate.split("-")
        val day = parts[2].toIntOrNull() ?: return
        val month = parts[1].toIntOrNull()?.minus(1) ?: return
        val year = parts[0].toIntOrNull() ?: return

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
        val hasError = !phoneNumber.isDigitsOnly() || phoneNumber.length < 8

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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())

                RegisterViewModel(
                    authRepository = AuthRepositoryImpl(directHealthApi = api),
                    savedStateHandle = createSavedStateHandle()
                )
            }
        }
    }
}