package com.uvg.directhealth.layouts.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val hasError: Boolean = false
)