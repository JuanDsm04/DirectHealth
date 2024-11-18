package com.uvg.directhealth.layouts.navigation.authentication

sealed interface AuthState {
    data object Loading: AuthState
    data object Authenticated: AuthState
    data object NonAuthenticated: AuthState
}