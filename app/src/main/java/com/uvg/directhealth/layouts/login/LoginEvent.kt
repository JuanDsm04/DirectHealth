package com.uvg.directhealth.layouts.login

sealed interface LoginEvent {
    data class EmailChange(val email: String): LoginEvent
    data class PasswordChange(val password: String): LoginEvent
    data object Login: LoginEvent
    data object IsPasswordVisibleChange: LoginEvent
}