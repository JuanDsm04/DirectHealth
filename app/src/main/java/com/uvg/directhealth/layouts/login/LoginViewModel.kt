package com.uvg.directhealth.layouts.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(): ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EmailChange -> onEmailChange(event.email)
            is LoginEvent.PasswordChange -> onPasswordChange(event.password)
            LoginEvent.IsPasswordVisibleChange -> onIsPasswordVisibleChange()
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
}