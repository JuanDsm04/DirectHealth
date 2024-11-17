package com.uvg.directhealth.layouts.mainFlow.profile

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.DoctorInfoDto
import com.uvg.directhealth.data.network.dto.UserDto
import com.uvg.directhealth.data.network.dto.toUser
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.domain.model.Specialty
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.model.Role
import com.uvg.directhealth.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.uvg.directhealth.util.Result
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

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
            is ProfileEvent.SaveProfile -> updateProfile()
        }
    }

    private fun formatDate(birthDateString: String): String {
        val formatInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatOutput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date = formatInput.parse(birthDateString) ?: Date()

        return formatOutput.format(date)
    }

    private fun formatDateToApiFormat(birthDateString: String): String {
        val formatInput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatOutput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val date: Date = formatInput.parse(birthDateString) ?: Date()

        return formatOutput.format(date)
    }

    private fun populateData(loggedUserId: String) {
        viewModelScope.launch {
            when (val result = userRepository.getUserById(loggedUserId)) {
                is Result.Success -> {
                    val loggedUser = result.data
                    val formattedBirthDate = formatDate(loggedUser.birthDate)

                    _state.update { state ->
                        state.copy(
                            loggedUser = loggedUser.toUser(),
                            name = loggedUser.name,
                            email = loggedUser.email,
                            password = loggedUser.password,
                            birthDate = formattedBirthDate,
                            dpi = loggedUser.dpi,
                            phoneNumber = loggedUser.phoneNumber,
                            medicalHistory = loggedUser.medicalHistory ?: "",
                            membership = loggedUser.doctorInfo?.number?.toString() ?: "",
                            address = loggedUser.doctorInfo?.address ?: "",
                            experience = loggedUser.doctorInfo?.summary ?: "",
                            specialty = Specialty.GENERAL,
                            isLoading = false,
                            hasError = false
                        )
                    }
                }

                is Result.Error -> {
                    _state.update { state ->
                        state.copy(
                            loggedUser = null,
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
            }
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

    private fun updateProfile() {
        val formattedBirthDate = formatDateToApiFormat(_state.value.birthDate)
        val doctorNumber = _state.value.membership.toIntOrNull() ?: 0

        val updatedUserDto = UserDto(
            id = _state.value.loggedUser?.id ?: "",
            role = _state.value.loggedUser?.role.toString(),
            name = _state.value.name,
            email = _state.value.email,
            password = _state.value.password,
            birthDate = formattedBirthDate,
            dpi = _state.value.dpi,
            phoneNumber = _state.value.phoneNumber,
            medicalHistory = _state.value.medicalHistory.takeIf { it.isNotEmpty() },
            doctorInfo = if (_state.value.loggedUser?.doctorInfo != null) {
                _state.value.loggedUser!!.doctorInfo?.let {
                    DoctorInfoDto(
                        number = doctorNumber,
                        address = _state.value.address,
                        summary = _state.value.experience,
                        specialty = _state.value.specialty.toString()
                    )
                }
            } else {
                null
            }
        )

        viewModelScope.launch {
            when (val result = userRepository.updateUser(updatedUserDto.id, updatedUserDto)) {
                is Result.Success -> {
                    _state.update { state ->
                        state.copy(
                            loggedUser = result.data.toUser(),
                            isLoading = false,
                            hasError = false
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            hasError = true
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val userRepository = UserRepositoryImpl(api)

                ProfileViewModel(userRepository = userRepository)
            }
        }
    }
}