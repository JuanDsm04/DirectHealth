package com.uvg.directhealth.layouts.mainFlow.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.dto.toUser
import com.uvg.directhealth.data.network.repository.UserRepositoryImpl
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.repository.UserRepository
import com.uvg.directhealth.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())
    val state = _state.asStateFlow()

    fun onEvent(event: UserProfileEvent) {
        when (event) {
            is UserProfileEvent.PopulateData -> populateData(event.loggedUserId, event.userProfileId)
            is UserProfileEvent.UpdateSelectedDate -> _state.update { it.copy(selectedDate = event.date) }
            is UserProfileEvent.UpdateSelectedTime -> _state.update { it.copy(selectedTime = event.time) }
            is UserProfileEvent.ToggleDatePicker -> {
                _state.update { it.copy(showDatePicker = event.show) }
            }
            is UserProfileEvent.ToggleTimePicker -> {
                _state.update { it.copy(showTimePicker = event.show) }
            }
        }
    }

    private fun populateData(loggedUserId: String, profileUserId: String) {
        viewModelScope.launch {
            val loggedUserResult = userRepository.getUserById(loggedUserId)
            val profileUserResult = userRepository.getUserById(profileUserId)

            if (loggedUserResult is Result.Success && profileUserResult is Result.Success) {
                val loggedUser = loggedUserResult.data.toUser()
                val userProfile = profileUserResult.data.toUser()

                _state.update { state ->
                    state.copy(
                        loggedUser = loggedUser,
                        userProfile = userProfile,
                        isLoading = false,
                        hasError = false
                    )
                }
            } else {
                _state.update {
                    it.copy(isLoading = false, hasError = true)
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())
                val userRepository = UserRepositoryImpl(api)

                UserProfileViewModel(userRepository = userRepository)
            }
        }
    }
}