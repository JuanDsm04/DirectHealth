package com.uvg.directhealth.layouts.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.uvg.directhealth.data.network.KtorDirectHealthApi
import com.uvg.directhealth.data.network.repository.AuthRepositoryImpl
import com.uvg.directhealth.di.KtorDependencies
import com.uvg.directhealth.domain.repository.AuthRepository
import com.uvg.directhealth.util.onError
import com.uvg.directhealth.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChange -> onEmailChange(event.email)
            is LoginEvent.PasswordChange -> onPasswordChange(event.password)
            LoginEvent.IsPasswordVisibleChange -> onIsPasswordVisibleChange()
            LoginEvent.Login -> onLogin()
        }
    }

    private fun onLogin() {
        viewModelScope.launch {
            authRepository
                .login(_state.value.email, _state.value.password)
                .onSuccess {
                    _state.update { state ->
                        state.copy(
                            successfulLogin = true
                        )
                    }
                }
                .onError {
                    _state.update { state ->
                        state.copy(
                            successfulLogin = false,
                            hasError = true
                        )
                    }
                }
        }
    }

    private fun onEmailChange(email: String) {
        _state.update { state ->
            state.copy(
                email = email,
                hasError = false
            )
        }
    }

    private fun onPasswordChange(password: String) {
        _state.update { state ->
            state.copy(
                password = password,
                hasError = false
            )
        }
    }

    private fun onIsPasswordVisibleChange() {
        _state.update { state ->
            state.copy(
                isPasswordVisible = !state.isPasswordVisible
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val api = KtorDirectHealthApi(KtorDependencies.provideHttpClient())

                LoginViewModel(
                    authRepository = AuthRepositoryImpl(directHealthApi = api)
                )
            }
        }
    }
}